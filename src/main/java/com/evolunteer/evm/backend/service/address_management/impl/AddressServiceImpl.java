package com.evolunteer.evm.backend.service.address_management.impl;

import com.evolunteer.evm.backend.repository.address_management.AddressRepository;
import com.evolunteer.evm.backend.service.address_management.AddressService;
import com.evolunteer.evm.common.domain.dto.address_management.AddressDto;
import com.evolunteer.evm.common.domain.entity.address_management.Address;
import com.evolunteer.evm.common.mapper.address_management.AddressMapper;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    @Transactional
    @Override
    public AddressDto create(final AddressDto addressDto) {
        ValidationUtils.validate(addressDto);
        final Address mappedAddress = addressMapper.mapAddressDtoToAddress(addressDto);
        return addressMapper.mapAddressToAddressDto(addressRepository.save(mappedAddress));
    }
}
