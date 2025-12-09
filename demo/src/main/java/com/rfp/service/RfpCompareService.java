package com.rfp.service;

import com.rfp.entity.ProposalEntity;
import com.rfp.repo.ProposalRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RfpCompareService {
    private final ProposalRepo proposalRepo;

    public RfpCompareService(ProposalRepo proposalRepo) {
        this.proposalRepo = proposalRepo;
    }

    public Map<String, Object> compare(Long rfpId) {
        List<ProposalEntity> proposals = proposalRepo.findByRfpId(rfpId);
        // simple scoring: lower price is better, faster delivery is better
        List<Map<String,Object>> scored = new ArrayList<>();
        for (ProposalEntity p : proposals) {
            double score = 0;
            if (p.getPrice() != null) score += (100000 - p.getPrice()) / 1000.0;
            if (p.getDeliveryDays() != null) score += (60 - p.getDeliveryDays());
            scored.add(Map.of(
                    "proposalId", p.getId(),
                    "vendorId", p.getVendorId(),
                    "price", p.getPrice(),
                    "deliveryDays", p.getDeliveryDays(),
                    "paymentTerms", p.getPaymentTerms(),
                    "warranty", p.getWarranty(),
                    "score", score
            ));
        }
        scored.sort(Comparator.comparingDouble(m -> -((Double)m.get("score"))));
        Map<String,Object> recommendation = scored.isEmpty() ? Map.of() : scored.get(0);
        return Map.of("proposals", scored, "recommendation", recommendation);
    }
}
