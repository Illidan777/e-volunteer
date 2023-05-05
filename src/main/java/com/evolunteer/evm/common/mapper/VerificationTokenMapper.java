package com.evolunteer.evm.common.mapper;

import com.evolunteer.evm.common.domain.dto.user_management.VerificationTokenDto;
import com.evolunteer.evm.common.domain.entity.user_management.VerificationToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class VerificationTokenMapper {

    public abstract VerificationTokenDto mapVerificationTokenToVerificationTokenDto(VerificationToken verificationToken);

    public abstract VerificationToken mapVerificationTokenDtoToVerificationToken(VerificationTokenDto verificationToken);
}
