package com.evolunteer.evm.backend.repository;

import com.evolunteer.evm.common.domain.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {
}
