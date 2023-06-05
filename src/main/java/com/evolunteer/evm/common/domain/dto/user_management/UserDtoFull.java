package com.evolunteer.evm.common.domain.dto.user_management;

import com.evolunteer.evm.common.domain.dto.fund_management.FundTeamRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserDtoFull extends BaseUserDto {
    private Set<FundTeamRequestDto> invitations = new HashSet<>();
}
