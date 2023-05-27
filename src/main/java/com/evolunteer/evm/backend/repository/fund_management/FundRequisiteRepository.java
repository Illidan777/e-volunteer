package com.evolunteer.evm.backend.repository.fund_management;

import com.evolunteer.evm.common.domain.entity.fund_management.FundRequisite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRequisiteRepository extends JpaRepository<FundRequisite, Long> {
}
