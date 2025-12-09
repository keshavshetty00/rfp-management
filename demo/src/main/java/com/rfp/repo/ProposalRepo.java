package com.rfp.repo;

import com.rfp.entity.ProposalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepo extends JpaRepository<ProposalEntity, Long> {
    List<ProposalEntity> findByRfpId(Long rfpId);
}
