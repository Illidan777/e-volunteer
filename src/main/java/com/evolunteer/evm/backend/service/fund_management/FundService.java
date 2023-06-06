package com.evolunteer.evm.backend.service.fund_management;

import com.evolunteer.evm.common.domain.dto.fund_management.*;
import com.evolunteer.evm.common.domain.enums.fund_management.FundActivityCategory;
import com.evolunteer.evm.common.domain.enums.fund_management.FundHelpRequestStatus;
import com.evolunteer.evm.common.domain.enums.fund_management.FundRequestType;
import com.evolunteer.evm.common.domain.request.fund_management.CreateFundRequest;
import com.evolunteer.evm.common.domain.request.fund_management.UpdateFundRequest;

import java.util.Optional;
import java.util.Set;

public interface FundService {

    FundDtoFull createFund(CreateFundRequest createFundRequest);

    FundDtoFull getFundById(Long fundId);

    void deleteRequisiteById(Long requisiteId);

    FundRequisiteDto addOrUpdateRequisite(Long fundId, FundRequisiteDto fundRequisiteDto);

    FundDtoFull updateFund(UpdateFundRequest updateFundRequest);

    void deleteMemberFromFund(Long userId);

    void createFundTeamRequest(Long userId, Long fundId, FundRequestType type);

    void processFundTeamRequest(Long requestId, boolean isAccept);

    Set<BaseFundDto> getAllFunds();

    Optional<FundTeamRequestDto> getFundTeamRequest(Long userId, Long fundId, FundRequestType type);

    Set<FundDtoFull> findFundByNameAndCategories(String name, Set<FundActivityCategory> categories);

    void createFundHelpRequest(Long fundId, FundHelpRequestDto fundHelpRequestDto);

    Optional<FundHelpRequestDto> getFundHelpRequestByNumber(String number);

    void updateFundHelpRequestStatus(Long requestId, FundHelpRequestStatus status);
}
