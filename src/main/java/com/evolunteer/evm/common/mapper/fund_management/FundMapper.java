package com.evolunteer.evm.common.mapper.fund_management;

import com.evolunteer.evm.common.domain.dto.fund_management.*;
import com.evolunteer.evm.common.domain.entity.fund_management.*;
import com.evolunteer.evm.common.domain.request.fund_management.CreateFundRequest;
import com.evolunteer.evm.common.domain.request.fund_management.UpdateFundRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class FundMapper {

    @Mappings(value = {
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "requisites", ignore = true),
            @Mapping(target = "requests", ignore = true),
            @Mapping(target = "helpRequests", ignore = true)
    })
    public abstract Fund mapCreateFundRequestToFund(CreateFundRequest createFundRequest);

    @Named("mapFundToFundDtoFull")
    public abstract FundDtoFull mapFundToFundDtoFull(Fund fund);

    @Named("mapFundToBaseFundDto")
    public abstract BaseFundDto mapFundToBaseFundDto(Fund fund);

    public abstract FundRequisite mapFundRequisiteDtoToFundRequisite(FundRequisiteDto fundRequisiteDto);

    public abstract Fund mapBaseFundDtoToFund(BaseFundDto baseFundDto);

    public abstract UpdateFundRequest mapFundDtoFullToUpdateFundRequest(FundDtoFull fundDtoFull);

    public abstract FundRequisiteDto mapFundRequisiteToFundRequisiteDto(FundRequisite fundRequisite);

    @Mappings(value = {
            @Mapping(target = "employees", ignore = true),
            @Mapping(target = "requisites", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "requests", ignore = true),
            @Mapping(target = "helpRequests", ignore = true)
    })
    public abstract Fund mapUpdateFundRequesToFund(UpdateFundRequest updateFundRequest, @MappingTarget Fund fund);

    public abstract FundTeamRequestDto mapFundTeamRequestToFundTeamRequestDto(FundTeamRequest request);

    public abstract HelpRequestExecutor mapHelpExecutorDtoToHelpExecutor(HelpRequestExecutorDto helpRequestExecutorDto);

    @Mappings(value = {
            @Mapping(target = "executor", ignore = true),
    })
    public abstract FundHelpRequest mapFundHelpRequestDtoToFundHelpRequest(FundHelpRequestDto fundHelpRequestDto);

    public abstract FundHelpRequestDto mapFundHelpRequestToFundHelpRequestDto(FundHelpRequest fundHelpRequest);
}
