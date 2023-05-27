package com.evolunteer.evm.backend.service.address_management;

import com.evolunteer.evm.common.domain.dto.address_management.AddressDto;

public interface AddressService {

    AddressDto create(AddressDto addressDto);
}
