package com.evolunteer.evm.backend.service.user_management.impl;

import com.evolunteer.evm.backend.repository.user_management.UserRepository;
import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.entity.user_management.User;
import com.evolunteer.evm.common.domain.enums.user_management.AccountAuthType;
import com.evolunteer.evm.common.domain.exception.validation.ValidationException;
import com.evolunteer.evm.common.domain.request.CreateAccountRequest;
import com.evolunteer.evm.common.domain.request.CreateExternalAccountRequest;
import com.evolunteer.evm.common.domain.request.CreateExternalUserRequest;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;
import com.evolunteer.evm.common.mapper.AccountMapper;
import com.evolunteer.evm.common.mapper.UserMapper;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
}
