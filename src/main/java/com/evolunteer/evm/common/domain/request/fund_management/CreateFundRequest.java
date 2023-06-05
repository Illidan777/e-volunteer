package com.evolunteer.evm.common.domain.request.fund_management;

import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import com.evolunteer.evm.common.domain.dto.fund_management.FundRequisiteDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateFundRequest extends BaseFundDto {
    private Set<FundRequisiteDto> requisites = new HashSet<>();
}
