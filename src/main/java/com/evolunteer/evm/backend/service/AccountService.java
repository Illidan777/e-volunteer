package com.evolunteer.evm.backend.service;

import com.evolunteer.evm.common.domain.dto.AccountDto;
import com.evolunteer.evm.common.domain.request.CreateAccountRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {

    AccountDto createAccount(CreateAccountRequest accountRequest);
}
