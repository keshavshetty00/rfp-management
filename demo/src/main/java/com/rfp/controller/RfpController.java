package com.rfp.controller;

import com.rfp.entity.RfpEntity;
import com.rfp.repo.RfpRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rfp")
public class RfpController {
    private final RfpRepo rfpRepo;

    public RfpController(RfpRepo rfpRepo) {
        this.rfpRepo = rfpRepo;
    }

    @GetMapping
    public List<RfpEntity> list() { return rfpRepo.findAll(); }
}
