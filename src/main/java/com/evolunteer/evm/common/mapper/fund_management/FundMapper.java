package com.evolunteer.evm.common.mapper.fund_management;

import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import com.evolunteer.evm.common.domain.dto.fund_management.FundDtoFull;
import com.evolunteer.evm.common.domain.dto.fund_management.FundRequisiteDto;
import com.evolunteer.evm.common.domain.entity.fund_management.Fund;
import com.evolunteer.evm.common.domain.entity.fund_management.FundRequisite;
import com.evolunteer.evm.common.domain.request.CreateFundRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class FundMapper {

    @Mappings(value = {
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "requisites", ignore = true)
    })
    public abstract Fund mapCreateFundRequestToFund(CreateFundRequest createFundRequest);

    public abstract FundDtoFull mapFundToFundDtoFull(Fund fund);

    public abstract FundRequisite mapFundRequisiteDtoToFundRequisite(FundRequisiteDto fundRequisiteDto);

    public abstract Fund mapBaseFundDtoToFund(BaseFundDto baseFundDto);
}
