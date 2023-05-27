package com.evolunteer.evm.common.domain.request;

import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateFundRequest extends BaseFundDto {
}
