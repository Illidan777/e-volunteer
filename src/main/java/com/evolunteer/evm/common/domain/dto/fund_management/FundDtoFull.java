package com.evolunteer.evm.common.domain.dto.fund_management;

import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundDtoFull extends BaseFundDto {
    private Set<UserDto> employees = new HashSet<>();
}
