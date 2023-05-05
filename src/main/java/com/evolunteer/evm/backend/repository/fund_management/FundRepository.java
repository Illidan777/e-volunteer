package com.evolunteer.evm.backend.repository.fund_management;

import com.evolunteer.evm.common.domain.entity.fund_management.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {
}
