package com.rfp.controller;

import com.rfp.entity.VendorEntity;
import com.rfp.service.VendorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public VendorEntity create(@RequestBody VendorEntity v) { return vendorService.create(v); }

    @GetMapping
    public List<VendorEntity> list() { return vendorService.list(); }
}
