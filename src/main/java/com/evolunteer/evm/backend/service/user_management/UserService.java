package com.evolunteer.evm.backend.service.user_management;

import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import com.evolunteer.evm.common.domain.dto.user_management.BaseUserDto;
import com.evolunteer.evm.common.domain.dto.user_management.UserDtoFull;
import com.evolunteer.evm.common.domain.request.user_management.CreateExternalUserRequest;
import com.evolunteer.evm.common.domain.request.user_management.CreateUserRequest;
import com.evolunteer.evm.common.domain.request.user_management.UpdateUserRequest;

import java.util.Set;

public interface UserService {

    BaseUserDto registerInternalUser(CreateUserRequest registrationRequest);

    BaseUserDto registerExternalUser(CreateExternalUserRequest registrationRequest);

    BaseUserDto getContextUser();

    void updateUserPicture(Long userId, FileMetaDataDto picture);

    void updateUser(Long userId, UpdateUserRequest updateUserRequest);

    void setFundToUser(Long userId, BaseFundDto fundDto);

    Set<BaseUserDto> getAllUsers();

    UserDtoFull getById(Long userId);
}
