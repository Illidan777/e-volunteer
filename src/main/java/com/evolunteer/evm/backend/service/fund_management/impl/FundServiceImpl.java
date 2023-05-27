package com.evolunteer.evm.backend.service.fund_management.impl;

import com.evolunteer.evm.backend.repository.fund_management.FundRepository;
import com.evolunteer.evm.backend.repository.fund_management.FundRequisiteRepository;
import com.evolunteer.evm.backend.service.address_management.AddressService;
import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.fund_management.FundDtoFull;
import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.entity.address_management.Address;
import com.evolunteer.evm.common.domain.entity.fund_management.Fund;
import com.evolunteer.evm.common.domain.entity.fund_management.FundRequisite;
import com.evolunteer.evm.common.domain.exception.common.ResourceNotFoundException;
import com.evolunteer.evm.common.domain.request.CreateFundRequest;
import com.evolunteer.evm.common.mapper.address_management.AddressMapper;
import com.evolunteer.evm.common.mapper.fund_management.FundMapper;
import com.evolunteer.evm.common.mapper.user_management.UserMapper;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {

    private final FundRepository fundRepository;
    private final FundRequisiteRepository fundRequisiteRepository;
    private final FundMapper fundMapper;
    private final AddressMapper addressMapper;
    private final UserMapper userMapper;
    private final AddressService addressService;
    private final UserService userService;

    @Transactional
    @Override
    public FundDtoFull createFund(final CreateFundRequest createFundRequest) {
        ValidationUtils.validate(createFundRequest);
        createFundRequest.getRequisites().forEach(ValidationUtils::validate);

        final UserDto contextUser = userService.getContextUser();
        final Address address = addressMapper.mapAddressDtoToAddress(addressService.create(createFundRequest.getAddress()));
        final Fund mappedFund = fundMapper.mapCreateFundRequestToFund(createFundRequest);
        mappedFund.setAddress(address);
        mappedFund.setCreatedBy(userMapper.mapUserDtoToUser(contextUser));
        final Fund savedFund = fundRepository.save(mappedFund);

        createFundRequest.getRequisites().forEach(fundRequisiteDto -> {
            final FundRequisite mappedRequisite = fundMapper.mapFundRequisiteDtoToFundRequisite(fundRequisiteDto);
            mappedRequisite.setFund(savedFund);
            fundRequisiteRepository.save(mappedRequisite);
        });
        final FundDtoFull fund = fundMapper.mapFundToFundDtoFull(this.findFundById(savedFund.getId()));
        userService.setFundToUser(contextUser.getId(), fund);
        return fund;
    }

    private Fund findFundById(final Long fundId) {
        Assert.notNull(fundId, "Unable to get fund by id. Id is null!");
        return fundRepository.findById(fundId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Fund by %s id does not exist", fundId)));
    }
}
