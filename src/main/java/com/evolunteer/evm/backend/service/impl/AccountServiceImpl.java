package com.evolunteer.evm.backend.service.impl;

import com.evolunteer.evm.backend.repository.AccountRepository;
import com.evolunteer.evm.backend.service.AccountService;
import com.evolunteer.evm.common.domain.dto.AccountDto;
import com.evolunteer.evm.common.domain.dto.AuthenticationPrincipal;
import com.evolunteer.evm.common.domain.entity.Account;
import com.evolunteer.evm.common.domain.enums.AccountRole;
import com.evolunteer.evm.common.domain.enums.AccountStatus;
import com.evolunteer.evm.common.domain.exception.AlreadyExistException;
import com.evolunteer.evm.common.domain.exception.ValidationException;
import com.evolunteer.evm.common.domain.request.CreateAccountRequest;
import com.evolunteer.evm.common.mapper.AccountMapper;
import com.evolunteer.evm.common.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if(StringUtils.isBlank(username)) {
            throw new ValidationException(String.format("Unable load account info by username %s. Username must be specified!", username));
        }
        return new AuthenticationPrincipal(this.getAccountByUsername(username));
    }

    @Override
    public AccountDto createAccount(final CreateAccountRequest accountRequest) {
        if(Objects.isNull(accountRequest)) {
            throw new ValidationException("Unable to create account. Account is null!");
        }
        ValidationUtils.validate(accountRequest);

        final String username = accountRequest.getUsername();
        final String password = accountRequest.getPassword();

        if(accountRepository.findByUsername(username).isPresent()) {
            throw new AlreadyExistException(String.format("Account already registered by username %s", username));
        }
        final Account mappedAccount = new Account();
        mappedAccount.setUsername(username);
        mappedAccount.setPassword(passwordEncoder.encode(password));
        mappedAccount.setStatus(AccountStatus.NOT_VERIFIED);
        mappedAccount.setRoles(new HashSet<>(Collections.singleton(AccountRole.ROLE_VOLUNTEER)));
        return accountMapper.mapAccountToAccountDto(accountRepository.save(mappedAccount));
    }

    private Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Account does not exist by username %s", username)));
    }
}
