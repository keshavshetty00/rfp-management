package com.rfp.repo;

import com.rfp.entity.RfpVendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RfpVendorRepo extends JpaRepository<RfpVendorEntity, Long> {
    List<RfpVendorEntity> findByRfpId(Long rfpId);
}
