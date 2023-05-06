package com.evolunteer.evm.backend.service.user_management;

import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;

import java.util.Optional;

public interface UserService {

    UserDto registerUser(CreateUserRequest registrationRequest);

    Optional<UserDto> getUserByUsername(String username);
}
