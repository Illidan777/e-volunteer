package com.evolunteer.evm.backend.repository.fund_management;

import com.evolunteer.evm.common.domain.entity.fund_management.FundTeamRequest;
import com.evolunteer.evm.common.domain.enums.fund_management.FundRequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundTeamRequestRepository extends JpaRepository<FundTeamRequest, Long> {

    Optional<FundTeamRequest> findByUser_IdAndFund_IdAndFundRequestType(Long userId, Long fundId, FundRequestType type);
}
