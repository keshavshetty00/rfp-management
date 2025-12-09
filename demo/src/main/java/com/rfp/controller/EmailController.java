package com.rfp.controller;

import com.rfp.dto.SendRfpRequest;
import com.rfp.entity.RfpEntity;
import com.rfp.entity.RfpVendorEntity;
import com.rfp.repo.RfpRepo;
import com.rfp.repo.RfpVendorRepo;
import com.rfp.repo.VendorRepo;
import com.rfp.service.EmailService;
import com.rfp.service.VendorService;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;
    private final VendorRepo vendorRepo;
    private final VendorService vendorService;
    private final RfpVendorRepo rfpVendorRepo;
    private final RfpRepo rfpRepo;
    private final ObjectMapper objectMapper;

    public EmailController(EmailService emailService, VendorRepo vendorRepo, VendorService vendorService, RfpVendorRepo rfpVendorRepo, RfpRepo rfpRepo) {
        this.emailService = emailService;
        this.vendorRepo = vendorRepo;
        this.vendorService = vendorService;
        this.rfpVendorRepo = rfpVendorRepo;
        this.rfpRepo = rfpRepo;
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping("/send")
    public String sendRfp(@RequestBody SendRfpRequest req) {
        // Fetch the RFP details
        RfpEntity rfp = rfpRepo.findById(req.getRfpId())
                .orElseThrow(() -> new RuntimeException("RFP not found with id: " + req.getRfpId()));
        
        vendorService.mapVendorsToRfp(req.getRfpId(), req.getVendorIds());
        List<String> emails = req.getVendorIds().stream()
                .map(id -> vendorRepo.findById(id).orElseThrow().getEmail())
                .collect(Collectors.toList());

        String subject = "RFP #" + req.getRfpId() + " - " + rfp.getTitle();
        String body = formatRfpEmailBody(rfp);
        
        System.out.println("====== SENDING EMAIL ======");
        System.out.println("From: keshavaganesh20@gmail.com");
        System.out.println("To: " + emails);
        System.out.println("Subject: " + subject);
        System.out.println("Body preview: " + body.substring(0, Math.min(200, body.length())));
        System.out.println("===========================");
        
        try {
            emailService.sendRfpEmail("keshavaganesh20@gmail.com", emails, subject, body);
            System.out.println("✅ Email sent successfully!");
        } catch (Exception e) {
            System.err.println("❌ Email sending failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
        
        return "Sent to " + emails.size() + " vendors";
    }
    
    private String formatRfpEmailBody(RfpEntity rfp) {
        StringBuilder body = new StringBuilder();
        body.append("Dear Vendor,\n\n");
        body.append("We are pleased to share the following RFP details:\n\n");
        body.append("═══════════════════════════════════════════════════\n");
        body.append("RFP DETAILS\n");
        body.append("═══════════════════════════════════════════════════\n\n");
        
        body.append("Title: ").append(rfp.getTitle()).append("\n");
        body.append("Budget: ₹").append(String.format("%,.2f", rfp.getBudget())).append("\n");
        body.append("Delivery Timeline: ").append(rfp.getDeliveryDays()).append(" days\n");
        body.append("Payment Terms: ").append(rfp.getPaymentTerms()).append("\n");
        body.append("Warranty: ").append(rfp.getWarranty()).append("\n\n");
        
        // Parse and display items
        try {
            if (rfp.getItemsJson() != null && !rfp.getItemsJson().isEmpty()) {
                JsonNode itemsNode = objectMapper.readTree(rfp.getItemsJson());
                if (itemsNode.isArray() && itemsNode.size() > 0) {
                    body.append("ITEMS REQUIRED:\n");
                    body.append("───────────────────────────────────────────────────\n");
                    int index = 1;
                    for (JsonNode item : itemsNode) {
                        body.append(index++).append(". ");
                        body.append(item.path("name").asText("Item")).append("\n");
                        body.append("   Quantity: ").append(item.path("qty").asText("N/A")).append("\n");
                        body.append("   Specifications: ").append(item.path("specs").asText("N/A")).append("\n\n");
                    }
                }
            }
        } catch (Exception e) {
            body.append("Items: Please refer to the detailed requirements.\n\n");
        }
        
        body.append("═══════════════════════════════════════════════════\n\n");
        body.append("Please review the above details and reply to this email with your proposal including:\n");
        body.append("• Your quoted price\n");
        body.append("• Delivery timeline\n");
        body.append("• Payment terms\n");
        body.append("• Warranty details\n\n");
        body.append("We look forward to your response.\n\n");
        body.append("Best regards,\n");
        body.append("RFP Team");
        
        return body.toString();
    }
}
