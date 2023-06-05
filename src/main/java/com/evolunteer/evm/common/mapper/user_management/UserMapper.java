package com.evolunteer.evm.common.mapper.user_management;

import com.evolunteer.evm.common.domain.dto.user_management.BaseUserDto;
import com.evolunteer.evm.common.domain.dto.user_management.UserDtoFull;
import com.evolunteer.evm.common.domain.entity.user_management.User;
import com.evolunteer.evm.common.domain.request.user_management.CreateExternalUserRequest;
import com.evolunteer.evm.common.domain.request.user_management.CreateUserRequest;
import com.evolunteer.evm.common.domain.request.user_management.UpdateUserRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract User mapRegistrationRequestToUser(CreateUserRequest createUserRequest);

    public abstract User mapRegistrationRequestToUser(CreateExternalUserRequest createUserRequest);

    @Named("mapUserToUserDto")
    public abstract BaseUserDto mapUserToUserDto(User user);

    public abstract User mapUserDtoToUser(BaseUserDto baseUserDto);

    public abstract UpdateUserRequest mapUserDtoToUpdateUserRequest(BaseUserDto baseUserDto);

    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "accountDetails", ignore = true),
            @Mapping(target = "picture", ignore = true),
            @Mapping(target = "fund", ignore = true)
    })
    public abstract User mapUpdateUserRequestToUser(UpdateUserRequest updateUserRequest, @MappingTarget User user);

    @Named("mapUserToUserDtoFull")
    public abstract UserDtoFull mapUserToUserDtoFull(User user);
}
