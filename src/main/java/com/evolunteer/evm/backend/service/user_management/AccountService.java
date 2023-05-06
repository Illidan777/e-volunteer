package com.evolunteer.evm.backend.service.user_management;

import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.enums.user_management.TokenVerificationResult;
import com.evolunteer.evm.common.domain.request.CreateAccountRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface AccountService extends UserDetailsService {

    AccountDto createAccount(CreateAccountRequest accountRequest);

    TokenVerificationResult verifyAccount(String encodedAccountId, String encodedVerificationToken);

    Optional<AccountDto> getAccountByUsername(String username);

    Optional<AccountDto> getAccountById(Long accountId);

    void recoverPasswordById(Long accountId, String newPassword);
}
