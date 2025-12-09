package com.rfp.repo;

import com.rfp.entity.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepo extends JpaRepository<VendorEntity, Long> { }
