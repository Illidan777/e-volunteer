package com.evolunteer.evm.backend.service;

import com.evolunteer.evm.common.domain.dto.UserDto;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;

public interface UserService {

    UserDto registerUser(CreateUserRequest registrationRequest);
}
