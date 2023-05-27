package com.evolunteer.evm.common.mapper.address_management;

import com.evolunteer.evm.common.domain.dto.address_management.AddressDto;
import com.evolunteer.evm.common.domain.entity.address_management.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AddressMapper {

    public abstract Address mapAddressDtoToAddress(AddressDto addressDto);

    public abstract AddressDto mapAddressToAddressDto(Address address);
}
