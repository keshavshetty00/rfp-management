package com.rfp.service;

import com.rfp.dto.InboundEmailRequestDto;
import com.rfp.entity.ProposalEntity;
import com.rfp.repo.ProposalRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EmailProcessingService {
    private final RfpAiService rfpAiService;
    private final ProposalRepo proposalRepo;

    public EmailProcessingService(RfpAiService rfpAiService, ProposalRepo proposalRepo) {
        this.rfpAiService = rfpAiService;
        this.proposalRepo = proposalRepo;
    }

    public ProposalEntity processInbound(InboundEmailRequestDto dto) {
        var parsed = rfpAiService.parseVendorEmail(dto.getBody());
        ProposalEntity p = new ProposalEntity();
        p.setRfpId(parsed.getRfpId() != null ? parsed.getRfpId() : dto.getRfpId());
        p.setVendorId(parsed.getVendorId() != null ? parsed.getVendorId() : dto.getVendorId());
        p.setPrice(parsed.getPrice());
        p.setDeliveryDays(parsed.getDeliveryDays());
        p.setPaymentTerms(parsed.getPaymentTerms());
        p.setWarranty(parsed.getWarranty());
        p.setRawResponse(dto.getBody());
        p.setParsedAt(Instant.now());
        return proposalRepo.save(p);
    }
}
