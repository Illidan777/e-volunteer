package com.evolunteer.evm.common.domain.dto.fund_management;

import com.evolunteer.evm.common.domain.dto.user_management.BaseUserDto;
import com.evolunteer.evm.common.domain.enums.fund_management.FundRequestStatus;
import com.evolunteer.evm.common.domain.enums.fund_management.FundRequestType;
import lombok.Data;

@Data
public class FundTeamRequestDto {
    private Long id;
    private FundRequestType fundRequestType;
    private FundRequestStatus status;
    private BaseFundDto fund;
    private BaseUserDto user;
}
