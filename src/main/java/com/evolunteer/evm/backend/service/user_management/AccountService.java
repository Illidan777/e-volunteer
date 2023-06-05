package com.evolunteer.evm.backend.service.user_management;

import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.enums.user_management.LinkVerificationResult;
import com.evolunteer.evm.common.domain.enums.user_management.VerificationLinkType;
import com.evolunteer.evm.common.domain.request.user_management.CreateAccountRequest;
import com.evolunteer.evm.common.domain.request.user_management.CreateExternalAccountRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AccountService extends UserDetailsService {

    AccountDto createInternalAccount(CreateAccountRequest accountRequest);

    AccountDto createExternalAccount(CreateExternalAccountRequest accountRequest);

    void sendVerificationLink(Long accountId, VerificationLinkType type);

    LinkVerificationResult verifyAccount(String encodedAccountId, String encodedVerificationToken);

    void recoverPasswordById(Long accountId, String newPassword);

    Optional<AccountDto> getAccountByUsername(String username);

    Optional<AccountDto> getAccountById(Long accountId);
}
