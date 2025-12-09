package com.rfp.repo;

import com.rfp.entity.RfpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RfpRepo extends JpaRepository<RfpEntity, Long> { }
