package com.rfp.controller;

import com.rfp.dto.GenerateRfpRequest;
import com.rfp.entity.RfpEntity;
import com.rfp.service.RfpAiService;
import com.rfp.service.RfpService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/rfp")
public class RfpAiController {
    private final RfpAiService aiService;
    private final RfpService rfpService;

    public RfpAiController(RfpAiService aiService, RfpService rfpService) {
        this.aiService = aiService;
        this.rfpService = rfpService;
    }

    @PostMapping("/generate")
    public RfpEntity generate(@Valid @RequestBody GenerateRfpRequest req) {
        Map<String,Object> structured = aiService.generateStructuredRfp(req.getDescription());
        return rfpService.saveStructuredRfp(structured);
    }

    @GetMapping("/{id}")
    public RfpEntity get(@PathVariable Long id) {
        return rfpService.get(id);
    }
}
