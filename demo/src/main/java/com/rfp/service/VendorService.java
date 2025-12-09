package com.rfp.service;

import com.rfp.entity.RfpVendorEntity;
import com.rfp.entity.VendorEntity;
import com.rfp.repo.RfpVendorRepo;
import com.rfp.repo.VendorRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VendorService {
    private final VendorRepo vendorRepo;
    private final RfpVendorRepo rfpVendorRepo;

    public VendorService(VendorRepo vendorRepo, RfpVendorRepo rfpVendorRepo) {
        this.vendorRepo = vendorRepo;
        this.rfpVendorRepo = rfpVendorRepo;
    }

    public VendorEntity create(VendorEntity v) { return vendorRepo.save(v); }

    public List<VendorEntity> list() { return vendorRepo.findAll(); }

    public void mapVendorsToRfp(Long rfpId, List<Long> vendorIds) {
        for (Long vid : vendorIds) {
            RfpVendorEntity m = new RfpVendorEntity();
            m.setRfpId(rfpId);
            m.setVendorId(vid);
            rfpVendorRepo.save(m);
        }
    }
}
