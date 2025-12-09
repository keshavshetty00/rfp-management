package com.rfp.controller;

import com.rfp.service.RfpCompareService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/compare")
public class RfpCompareController {
    private final RfpCompareService compareService;

    public RfpCompareController(RfpCompareService compareService) {
        this.compareService = compareService;
    }

    @GetMapping("/{rfpId}")
    public Map<String,Object> compare(@PathVariable Long rfpId) {
        return compareService.compare(rfpId);
    }
}
