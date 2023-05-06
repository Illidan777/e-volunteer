package com.evolunteer.evm.backend.service.user_management.impl;

import com.evolunteer.evm.backend.repository.user_management.UserRepository;
import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.entity.user_management.User;
import com.evolunteer.evm.common.domain.exception.validation.ValidationException;
import com.evolunteer.evm.common.domain.request.CreateAccountRequest;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;
import com.evolunteer.evm.common.mapper.AccountMapper;
import com.evolunteer.evm.common.mapper.UserMapper;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto registerUser(final CreateUserRequest registrationRequest) {
        if (Objects.isNull(registrationRequest)) {
            throw new ValidationException("Unable to create user. Registration request is null!");
        }
        ValidationUtils.validate(registrationRequest);
        final AccountDto createdAccount = accountService.createAccount(new CreateAccountRequest(registrationRequest.getUsername(), registrationRequest.getPassword(), registrationRequest.getEmail()));

        final User mappedUser = userMapper.mapRegistrationRequestToUser(registrationRequest);
        mappedUser.setAccountDetails(accountMapper.mapAccountDtoToAccount(createdAccount));
        return userMapper.mapUserToUserDto(userRepository.save(mappedUser));
    }

    @Override
    public Optional<UserDto> getUserByUsername(final String username) {
        if (StringUtils.isBlank(username)) {
            throw new ValidationException("Unable to get user by username. Username is missing!");
        }
        final Optional<User> optionalUser = userRepository.findByAccountDetails_Username(username);
        return optionalUser.map(userMapper::mapUserToUserDto);
    }
}
