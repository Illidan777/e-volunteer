package com.evolunteer.evm.backend.service.user_management.impl;

import com.evolunteer.evm.backend.repository.user_management.AccountRepository;
import com.evolunteer.evm.backend.security.config.VerificationTokenConfig;
import com.evolunteer.evm.backend.service.notification_management.sender.NotificationService;
import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.backend.service.user_management.VerificationTokenService;
import com.evolunteer.evm.common.domain.dto.notification_management.NotificationRequest;
import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.backend.security.principal.AuthenticationPrincipal;
import com.evolunteer.evm.common.domain.dto.user_management.VerificationTokenDto;
import com.evolunteer.evm.common.domain.entity.user_management.Account;
import com.evolunteer.evm.common.domain.enums.notification_management.NotificationProviderType;
import com.evolunteer.evm.common.domain.enums.user_management.AccountRole;
import com.evolunteer.evm.common.domain.enums.user_management.AccountStatus;
import com.evolunteer.evm.common.domain.enums.user_management.TokenVerificationResult;
import com.evolunteer.evm.common.domain.exception.common.AlreadyExistException;
import com.evolunteer.evm.common.domain.exception.common.ResourceNotFoundException;
import com.evolunteer.evm.common.domain.exception.validation.ValidationException;
import com.evolunteer.evm.common.domain.request.CreateAccountRequest;
import com.evolunteer.evm.common.mapper.AccountMapper;
import com.evolunteer.evm.common.mapper.VerificationTokenMapper;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.string.StringGenerator;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.EmailNotification.ACCOUNT_VERIFICATION_NOTIFICATION_PATTERN;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.EmailNotification.ACCOUNT_VERIFICATION_NOTIFICATION_SUBJECT;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final VerificationTokenService verificationTokenService;
    private final NotificationService notificationService;
    private final VerificationTokenConfig verificationTokenConfig;
    private final VerificationTokenMapper verificationTokenMapper;
    private final MessageSource messageSource;

    @Value("${email.verification.link.prefix}")
    private String verificationLinkPrefix;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if(StringUtils.isBlank(username)) {
            throw new ValidationException(String.format("Unable load account info by username %s. Username must be specified!", username));
        }
        return new AuthenticationPrincipal(this.findAccountByUsername(username));
    }

    @Override
    public AccountDto createAccount(final CreateAccountRequest accountRequest) {
        if(Objects.isNull(accountRequest)) {
            throw new ValidationException("Unable to create account. Account is null!");
        }
        ValidationUtils.validate(accountRequest);

        final String username = accountRequest.getUsername();
        final String password = accountRequest.getPassword();

        final Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if(optionalAccount.isPresent()) {
            final Account existedAccount = optionalAccount.get();
            if(existedAccount.getStatus().isExpired()) {
                accountRepository.delete(existedAccount);
            } else {
                throw new AlreadyExistException(
                        format("Account have already registered by email: %s", username));
            }
        }
        final String generatedToken = new StringGenerator.StringGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useLower(true)
                .useUpper(true)
                .build()
                .generate(verificationTokenConfig.getVerificationTokenLength());
        final VerificationTokenDto verificationToken = verificationTokenService.create(generatedToken);
        final String encodedToken = Base64.getEncoder().encodeToString(generatedToken.getBytes());

        final Account mappedAccount = new Account();
        mappedAccount.setUsername(username);
        mappedAccount.setPassword(passwordEncoder.encode(password));
        mappedAccount.setStatus(AccountStatus.NOT_VERIFIED);
        mappedAccount.setRoles(new HashSet<>(Collections.singleton(AccountRole.ROLE_VOLUNTEER)));
        mappedAccount.setVerificationToken(verificationTokenMapper.mapVerificationTokenDtoToVerificationToken(verificationToken));
        final Account savedAccount = accountRepository.save(mappedAccount);

        final String encodedAccountId = Base64.getEncoder().encodeToString(String.valueOf(savedAccount.getId()).getBytes());
        this.sendVerificationNotification(accountRequest, encodedToken, encodedAccountId);

        return accountMapper.mapAccountToAccountDto(savedAccount);
    }

    @Override
    public TokenVerificationResult verifyAccount(final String encodedAccountId, final String encodedVerificationToken) {
        final Long decodedAccountId;
        try {
            decodedAccountId = Long.valueOf(new String(Base64.getDecoder().decode(encodedAccountId)));
        }catch (IllegalArgumentException e) {
            return TokenVerificationResult.INCORRECT_VERIFICATION_CREDENTIALS;
        }
        final Account account = this.findAccountById(decodedAccountId);

        if (account.getStatus().isVerified()) {
            return TokenVerificationResult.SUCCESSFUL_VERIFICATION;
        }
        final TokenVerificationResult tokenVerificationResult = verificationTokenService.verifyToken(account.getVerificationToken().getId(), encodedVerificationToken);
        if (tokenVerificationResult.isExpired() || account.getStatus().isExpired()) {
            this.updateAccountStatus(decodedAccountId, AccountStatus.EXPIRED);
        }
        if (tokenVerificationResult.isSuccess()) {
            this.updateAccountStatus(decodedAccountId, AccountStatus.VERIFIED);
        }
        return tokenVerificationResult;
    }

    @Override
    public Optional<AccountDto> getAccountByUsername(final String username) {
        final Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        return optionalAccount.map(accountMapper::mapAccountToAccountDto);
    }

    public void updateAccountStatus(final Long accountId, final AccountStatus status) {
        if (Objects.isNull(accountId) || Objects.isNull(status)) {
            throw new ValidationException("Unable to update account status. Account id and status can no be absent");
        }
        final Account account = accountRepository.findById(accountId).orElseThrow(() ->
                new ResourceNotFoundException(format("Account does not exist by id: %s", accountId)));
        account.setStatus(status);
        accountRepository.save(account);
    }

    private void sendVerificationNotification(final CreateAccountRequest createAccountRequest,
                                              final String encodedToken,
                                              final String encodedAccountId) {
        final String verificationLink = String.format(verificationLinkPrefix, encodedAccountId, encodedToken);
        final String verificationAccountSubject = messageSource.getMessage(ACCOUNT_VERIFICATION_NOTIFICATION_SUBJECT,
                null, LocalizationUtils.getLocale());
        final String verificationAccountPattern = messageSource.getMessage(ACCOUNT_VERIFICATION_NOTIFICATION_PATTERN,
                null, LocalizationUtils.getLocale());
        final NotificationRequest notificationRequest = NotificationRequest.of(
                String.format(verificationAccountPattern, verificationLink, createAccountRequest.getUsername(), createAccountRequest.getPassword()),
                verificationAccountSubject,
                NotificationProviderType.EMAIL,
                Set.of(createAccountRequest.getEmail())
        );
        notificationService.send(notificationRequest);
    }

    private Account findAccountByUsername(final String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Account does not exist by username %s", username)));
    }

    private Account findAccountById(final Long accountId) {
        Assert.notNull(accountId, "Unable to get accounts by id. Id is null!");
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Account by %s id does not exist", accountId)));
    }
}
