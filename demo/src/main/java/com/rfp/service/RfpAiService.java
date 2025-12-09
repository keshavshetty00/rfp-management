package com.rfp.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rfp.dto.ProposalDto;

@Service
public class RfpAiService {
    @Value("${ai.api.key:}")
    private String apiKey;

    // For demo: enhanced parsing implementation. Replace with HTTP call to your AI provider for production.
    public Map<String, Object> generateStructuredRfp(String description) {
        String lowerDesc = description.toLowerCase();
        
        // Extract title - look for key phrases
        String title = "Procurement Request";
        if (lowerDesc.contains("laptop")) title = "Laptop Procurement Request";
        else if (lowerDesc.contains("furniture")) title = "Furniture Procurement Request";
        else if (lowerDesc.contains("software")) title = "Software Procurement Request";
        
        // Extract budget - look for "budget is" or "budget:" followed by currency and amount
        double budget = 0;
        java.util.regex.Pattern budgetPattern = java.util.regex.Pattern.compile("budget\\s+(?:is)?\\s*[₹$]\\s*([0-9,]+)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher budgetMatcher = budgetPattern.matcher(description);
        if (budgetMatcher.find()) {
            String budgetStr = budgetMatcher.group(1).replace(",", "");
            try { budget = Double.parseDouble(budgetStr); } catch (Exception e) {}
        }
        
        // Extract delivery timeline - look for "delivery within" or "delivery in" followed by number and days
        int delivery = 30; // default
        java.util.regex.Pattern deliveryPattern = java.util.regex.Pattern.compile("delivery\\s+(?:within|in)?\\s*(\\d+)\\s*days?", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher deliveryMatcher = deliveryPattern.matcher(description);
        if (deliveryMatcher.find()) {
            try { delivery = Integer.parseInt(deliveryMatcher.group(1)); } catch (Exception e) {}
        }
        
        // Extract payment terms
        String paymentTerms = "Net 30";
        java.util.regex.Pattern paymentPattern = java.util.regex.Pattern.compile("payment\\s+terms?\\s+net\\s+(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher paymentMatcher = paymentPattern.matcher(description);
        if (paymentMatcher.find()) {
            paymentTerms = "Net " + paymentMatcher.group(1);
        } else if (lowerDesc.contains("advance")) {
            paymentTerms = "50% Advance, 50% on Delivery";
        }
        
        // Extract warranty
        String warranty = "1 year";
        java.util.regex.Pattern warrantyPattern = java.util.regex.Pattern.compile("warranty\\s+(\\d+)\\s*years?", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher warrantyMatcher = warrantyPattern.matcher(description);
        if (warrantyMatcher.find()) {
            String years = warrantyMatcher.group(1);
            warranty = years.equals("1") ? "1 year" : years + " years";
        }
        
        // Parse items from description
        Object[] items = parseItems(description);
        
        return Map.of(
                "title", title,
                "budget", budget,
                "delivery_days", delivery,
                "payment_terms", paymentTerms,
                "warranty", warranty,
                "items", items
        );
    }
    
    private Object[] parseItems(String description) {
        String lowerDesc = description.toLowerCase();
        java.util.List<Map<String, Object>> itemsList = new java.util.ArrayList<>();
        
        // Extract laptop specifications
        if (lowerDesc.contains("laptop")) {
            int qty = extractQuantity(description, "laptop");
            String specs = extractSpecs(description, "laptop", new String[]{"ram", "gb", "processor", "storage"});
            itemsList.add(Map.of("name", "laptop", "qty", qty, "specs", specs));
        }
        
        // Extract monitor specifications
        if (lowerDesc.contains("monitor")) {
            int qty = extractQuantity(description, "monitor");
            String specs = extractSpecs(description, "monitor", new String[]{"inch", "resolution", "hz"});
            itemsList.add(Map.of("name", "monitor", "qty", qty, "specs", specs));
        }
        
        // Add more item types as needed
        
        return itemsList.isEmpty() ? 
            new Object[]{Map.of("name", "item", "qty", 1, "specs", "As per description")} : 
            itemsList.toArray();
    }
    
    private int extractQuantity(String description, String itemName) {
        String pattern = "(\\d+)\\s*" + itemName;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern, java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher m = p.matcher(description);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (Exception e) {}
        }
        return 1;
    }
    
    private String extractSpecs(String description, String itemName, String[] keywords) {
        StringBuilder specs = new StringBuilder();
        String lowerDesc = description.toLowerCase();
        
        for (String keyword : keywords) {
            int idx = lowerDesc.indexOf(keyword);
            if (idx != -1) {
                // Extract surrounding context
                int start = Math.max(0, idx - 10);
                int end = Math.min(lowerDesc.length(), idx + keyword.length() + 20);
                String context = description.substring(start, end).trim();
                if (specs.length() > 0) specs.append(", ");
                specs.append(context);
            }
        }
        
        return specs.length() > 0 ? specs.toString() : "Standard specifications";
    }

    public ProposalDto parseVendorEmail(String body) {
        ProposalDto dto = new ProposalDto();
        
        // Extract price from various formats: ₹4,95,000.00 or $48000 or 495000
        Double price = extractPrice(body);
        dto.setPrice(price);
        
        // Extract delivery days: "Within 40 days" or "40 days from" or ":0 days"
        Integer delivery = extractDeliveryDays(body);
        dto.setDeliveryDays(delivery);
        
        // Extract payment terms: "Net 20 days" or "Net 15 days" etc.
        String paymentTerms = extractPaymentTerms(body);
        dto.setPaymentTerms(paymentTerms);
        
        // Extract warranty: "2 years" or "2+1 years" or "1 year"
        String warranty = extractWarranty(body);
        dto.setWarranty(warranty);
        
        return dto;
    }
    
    private Double extractPrice(String body) {
        // Try to find price with currency symbol and commas: ₹4,95,000.00 or $48,000
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[₹$]\\s*([0-9,]+\\.?[0-9]*)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            String priceStr = matcher.group(1).replaceAll(",", "");
            try {
                return Double.parseDouble(priceStr);
            } catch (Exception e) {}
        }
        
        // Fallback: look for "Price:" or "Quoted Price:" followed by numbers
        pattern = java.util.regex.Pattern.compile("(?:quoted\\s+)?price[:\\s]+[₹$]?\\s*([0-9,]+\\.?[0-9]*)", java.util.regex.Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(body);
        if (matcher.find()) {
            String priceStr = matcher.group(1).replaceAll(",", "");
            try {
                return Double.parseDouble(priceStr);
            } catch (Exception e) {}
        }
        
        return 0.0; // default if no price found
    }
    
    private Integer extractDeliveryDays(String body) {
        // Pattern 1: "Within 40 days" or "within :0 days"
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("within\\s+:?(\\d+)\\s+days?", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (Exception e) {}
        }
        
        // Pattern 2: "40 days from" or "25 days"
        pattern = java.util.regex.Pattern.compile("(\\d+)\\s+days?", java.util.regex.Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(body);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (Exception e) {}
        }
        
        return 30; // default
    }
    
    private String extractPaymentTerms(String body) {
        // Look for "Net XX days" or "Net XX"
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("net\\s+(\\d+)(?:\\s+days?)?", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return "Net " + matcher.group(1) + " days";
        }
        
        // Look for advance payment terms
        if (body.toLowerCase().contains("advance")) {
            pattern = java.util.regex.Pattern.compile("(\\d+)%\\s+advance", java.util.regex.Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(body);
            if (matcher.find()) {
                return matcher.group(1) + "% Advance";
            }
        }
        
        return "Net 30"; // default
    }
    
    private String extractWarranty(String body) {
        // Pattern 1: "2+1 years" or "2 + 1 years"
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s*\\+\\s*(\\d+)\\s+years?", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(1) + "+" + matcher.group(2) + " years";
        }
        
        // Pattern 2: "2 years" or "1 year"
        pattern = java.util.regex.Pattern.compile("(\\d+)\\s+years?", java.util.regex.Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(body);
        if (matcher.find()) {
            String years = matcher.group(1);
            return years.equals("1") ? "1 year" : years + " years";
        }
        
        return "1 year"; // default
    }
}
