package com.evolunteer.evm.backend.repository.fund_management;

import com.evolunteer.evm.common.domain.entity.fund_management.FundHelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundHelpRequestRepository extends JpaRepository<FundHelpRequest, Long> {

    Optional<FundHelpRequest> findByNumber(String number);
}
