package com.evolunteer.evm.common.mapper;

import com.evolunteer.evm.common.domain.dto.user_management.VerificationLinkDto;
import com.evolunteer.evm.common.domain.entity.user_management.VerificationLink;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class VerificationLinkMapper {

    public abstract VerificationLinkDto mapVerificationLinkToVerificationLinkDto(VerificationLink verificationLink);

    public abstract VerificationLink mapVerificationLinkDtoToVerificationLink(VerificationLinkDto verificationToken);
}
