package com.evolunteer.evm.backend.service.user_management;

import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.enums.user_management.LinkVerificationResult;
import com.evolunteer.evm.common.domain.request.CreateAccountRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AccountService extends UserDetailsService {

    AccountDto createAccount(CreateAccountRequest accountRequest);

    LinkVerificationResult verifyAccount(String encodedAccountId, String encodedVerificationToken);

    void recoverPasswordById(Long accountId, String newPassword);

    Optional<AccountDto> getAccountByUsername(String username);

    Optional<AccountDto> getAccountById(Long accountId);

    void createPasswordRecover(Long accountId);
}
