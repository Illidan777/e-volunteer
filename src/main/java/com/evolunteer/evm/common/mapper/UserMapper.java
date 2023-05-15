package com.evolunteer.evm.common.mapper;

import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.entity.user_management.User;
import com.evolunteer.evm.common.domain.request.CreateExternalUserRequest;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract User mapRegistrationRequestToUser(CreateUserRequest createUserRequest);

    public abstract User mapRegistrationRequestToUser(CreateExternalUserRequest createUserRequest);

    public abstract UserDto mapUserToUserDto(User user);
}
