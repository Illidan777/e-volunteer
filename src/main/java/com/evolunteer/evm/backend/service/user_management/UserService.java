package com.evolunteer.evm.backend.service.user_management;

import com.evolunteer.evm.common.domain.dto.file_management.EmbeddableFile;
import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.request.CreateExternalUserRequest;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;
import com.evolunteer.evm.common.domain.request.UpdateUserRequest;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    UserDto registerInternalUser(CreateUserRequest registrationRequest);

    UserDto registerExternalUser(CreateExternalUserRequest registrationRequest);

    UserDto getByAccountId(Long accountId);

    void updateUserPicture(Long userId, EmbeddableFile picture);

    void updateUser(Long userId, UpdateUserRequest updateUserRequest);
}
