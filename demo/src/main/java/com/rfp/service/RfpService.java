package com.rfp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfp.entity.RfpEntity;
import com.rfp.repo.RfpRepo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RfpService {
    private final RfpRepo rfpRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public RfpService(RfpRepo rfpRepo) {
        this.rfpRepo = rfpRepo;
    }

    public RfpEntity saveStructuredRfp(Map<String, Object> rfpStruct) {
        RfpEntity rfp = new RfpEntity();
        rfp.setTitle((String) rfpStruct.getOrDefault("title", "RFP"));
        rfp.setBudget(((Number) rfpStruct.getOrDefault("budget", 0)).doubleValue());
        rfp.setDeliveryDays(((Number) rfpStruct.getOrDefault("delivery_days", 30)).intValue());
        rfp.setPaymentTerms((String) rfpStruct.getOrDefault("payment_terms", "Net 30"));
        rfp.setWarranty((String) rfpStruct.getOrDefault("warranty", "1 year"));
        try {
            rfp.setItemsJson(mapper.writeValueAsString(rfpStruct.getOrDefault("items", Map.of())));
        } catch (Exception ignored) { }
        return rfpRepo.save(rfp);
    }

    public RfpEntity get(Long id) {
        return rfpRepo.findById(id).orElseThrow();
    }
}
