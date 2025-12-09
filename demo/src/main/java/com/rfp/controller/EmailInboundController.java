package com.rfp.controller;

import com.rfp.dto.InboundEmailRequestDto;
import com.rfp.entity.ProposalEntity;
import com.rfp.service.EmailProcessingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailInboundController {
    private final EmailProcessingService emailProcessingService;

    public EmailInboundController(EmailProcessingService emailProcessingService) {
        this.emailProcessingService = emailProcessingService;
    }

    // Simulated inbound email hook (webhook or manual POST)
    @PostMapping("/inbound")
    public ProposalEntity inbound(@Valid @RequestBody InboundEmailRequestDto dto) {
        return emailProcessingService.processInbound(dto);
    }
}
