package com.evolunteer.evm.common.domain.dto.fund_management;

import com.evolunteer.evm.common.domain.dto.user_management.BaseUserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundDtoFull extends BaseFundDto {
    private BaseUserDto createdBy;
    private Set<BaseUserDto> employees = new HashSet<>();
    private Set<FundRequisiteDto> requisites = new HashSet<>();
    private Set<FundTeamRequestDto> requests = new HashSet<>();
    private Set<FundHelpRequestDto> helpRequests = new HashSet<>();
}
