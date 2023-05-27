package com.evolunteer.evm.backend.service.fund_management;

import com.evolunteer.evm.common.domain.dto.fund_management.FundDtoFull;
import com.evolunteer.evm.common.domain.request.CreateFundRequest;

public interface FundService {

    FundDtoFull createFund(CreateFundRequest createFundRequest);
}
