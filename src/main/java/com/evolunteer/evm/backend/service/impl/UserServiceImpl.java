package com.evolunteer.evm.backend.service.impl;

import com.evolunteer.evm.backend.repository.UserRepository;
import com.evolunteer.evm.backend.service.AccountService;
import com.evolunteer.evm.backend.service.UserService;
import com.evolunteer.evm.common.domain.dto.AccountDto;
import com.evolunteer.evm.common.domain.dto.UserDto;
import com.evolunteer.evm.common.domain.entity.User;
import com.evolunteer.evm.common.domain.exception.ValidationException;
import com.evolunteer.evm.common.domain.request.CreateAccountRequest;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;
import com.evolunteer.evm.common.mapper.AccountMapper;
import com.evolunteer.evm.common.mapper.UserMapper;
import com.evolunteer.evm.common.utils.ValidationUtils;
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
    public UserDto registerUser(final CreateUserRequest registrationRequest) {
        if(Objects.isNull(registrationRequest)) {
            throw new ValidationException("Unable to create user. Registration request is null!");
        }
        ValidationUtils.validate(registrationRequest);
        final AccountDto createdAccount = accountService.createAccount(new CreateAccountRequest(registrationRequest.getUsername(), registrationRequest.getPassword()));

        final User mappedUser = userMapper.mapRegistrationRequestToUser(registrationRequest);
        mappedUser.setAccountDetails(accountMapper.mapAccountDtoToAccount(createdAccount));
        return userMapper.mapUserToUserDto(userRepository.save(mappedUser));
    }
}
