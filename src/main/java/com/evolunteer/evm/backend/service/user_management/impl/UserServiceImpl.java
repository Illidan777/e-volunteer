package com.evolunteer.evm.backend.service.user_management.impl;

import com.evolunteer.evm.backend.repository.user_management.UserRepository;
import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.file_management.EmbeddableFile;
import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.entity.user_management.User;
import com.evolunteer.evm.common.domain.exception.common.ResourceNotFoundException;
import com.evolunteer.evm.common.domain.exception.validation.ValidationException;
import com.evolunteer.evm.common.domain.request.*;
import com.evolunteer.evm.common.mapper.user_management.AccountMapper;
import com.evolunteer.evm.common.mapper.user_management.UserMapper;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto registerInternalUser(final CreateUserRequest registrationRequest) {
        if (Objects.isNull(registrationRequest)) {
            throw new ValidationException("Unable to create user. Registration request is null!");
        }
        ValidationUtils.validate(registrationRequest);
        final AccountDto createdAccount = accountService.createInternalAccount(new CreateAccountRequest(registrationRequest.getUsername(), registrationRequest.getPassword(), registrationRequest.getEmail()));

        final User mappedUser = userMapper.mapRegistrationRequestToUser(registrationRequest);
        mappedUser.setAccountDetails(accountMapper.mapAccountDtoToAccount(createdAccount));
        return userMapper.mapUserToUserDto(userRepository.save(mappedUser));
    }

    @Transactional
    @Override
    public UserDto registerExternalUser(final CreateExternalUserRequest registrationRequest) {
        if (Objects.isNull(registrationRequest)) {
            throw new ValidationException("Unable to create external user. Registration request is null!");
        }
        ValidationUtils.validate(registrationRequest);
        final AccountDto createdAccount = accountService.createExternalAccount(new CreateExternalAccountRequest(registrationRequest.getUsername(), registrationRequest.getAuthType()));

        final User mappedUser = userMapper.mapRegistrationRequestToUser(registrationRequest);
        mappedUser.setAccountDetails(accountMapper.mapAccountDtoToAccount(createdAccount));
        return userMapper.mapUserToUserDto(userRepository.save(mappedUser));
    }

    @Override
    public UserDto getByAccountId(final Long accountId) {
        return userMapper.mapUserToUserDto(this.getUserByAccountId(accountId));
    }

    @Override
    public void updateUserPicture(final Long userId, final EmbeddableFile picture) {
        final User user = this.getUserById(userId);
        if(Objects.isNull(picture)) {
            user.setPicture(null);
        } else {
            ValidationUtils.validate(picture);
            user.setPicture(picture);
        }
        userRepository.save(user);
    }

    @Override
    public void updateUser(final Long userId, final UpdateUserRequest updateUserRequest) {
        if (Objects.isNull(userId) || Objects.isNull(updateUserRequest)) {
            throw new ValidationException("Unable to update user. Update user request or id is null!");
        }
        final User user = this.getUserById(userId);
        ValidationUtils.validate(updateUserRequest);
        userRepository.save(userMapper.mapUpdateUserRequestToUser(updateUserRequest, user));
    }

    private User getUserById(final Long userId) {
        if (Objects.isNull(userId)) {
            throw new ValidationException("Unable to get user by id. Id must be specified!");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(format("User does not exist by id: %s", userId)));
    }

    private User getUserByAccountId(final Long accountId) {
        if (Objects.isNull(accountId)) {
            throw new ValidationException("Unable to get user by account id. Id must be specified!");
        }
        return userRepository.findByAccountDetails_Id(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(format("User does not exist by account id: %s", accountId)));
    }
}
