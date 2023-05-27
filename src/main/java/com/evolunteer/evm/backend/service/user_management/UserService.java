package com.evolunteer.evm.backend.service.user_management;

import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.request.CreateExternalUserRequest;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;
import com.evolunteer.evm.common.domain.request.UpdateUserRequest;

public interface UserService {

    UserDto registerInternalUser(CreateUserRequest registrationRequest);

    UserDto registerExternalUser(CreateExternalUserRequest registrationRequest);

    UserDto getContextUser();

    void updateUserPicture(Long userId, FileMetaDataDto picture);

    void updateUser(Long userId, UpdateUserRequest updateUserRequest);

    void setFundToUser(Long userId, BaseFundDto fundDto);
}
