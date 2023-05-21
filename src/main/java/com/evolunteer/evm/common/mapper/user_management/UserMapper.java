package com.evolunteer.evm.common.mapper.user_management;

import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.entity.user_management.User;
import com.evolunteer.evm.common.domain.request.CreateExternalUserRequest;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;
import com.evolunteer.evm.common.domain.request.UpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract User mapRegistrationRequestToUser(CreateUserRequest createUserRequest);

    public abstract User mapRegistrationRequestToUser(CreateExternalUserRequest createUserRequest);

    public abstract UserDto mapUserToUserDto(User user);

    public abstract UpdateUserRequest mapUserDtoToUpdateUserRequest(UserDto userDto);

    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "accountDetails", ignore = true),
            @Mapping(target = "picture", ignore = true)
    })
    public abstract User mapUpdateUserRequestToUser(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}
