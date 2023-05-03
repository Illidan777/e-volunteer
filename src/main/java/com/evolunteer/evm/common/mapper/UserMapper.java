package com.evolunteer.evm.common.mapper;

import com.evolunteer.evm.common.domain.dto.UserDto;
import com.evolunteer.evm.common.domain.entity.User;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract User mapRegistrationRequestToUser(CreateUserRequest createUserRequest);

    public abstract UserDto mapUserToUserDto(User user);
}
