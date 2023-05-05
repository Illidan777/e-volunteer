package com.evolunteer.evm.backend.service.user_management.impl;

import com.evolunteer.evm.backend.repository.user_management.VerificationTokenRepository;
import com.evolunteer.evm.backend.security.config.VerificationTokenConfig;
import com.evolunteer.evm.backend.service.user_management.VerificationTokenService;
import com.evolunteer.evm.common.domain.dto.user_management.VerificationTokenDto;
import com.evolunteer.evm.common.domain.entity.user_management.VerificationToken;
import com.evolunteer.evm.common.domain.enums.user_management.TokenVerificationResult;
import com.evolunteer.evm.common.domain.exception.common.ResourceNotFoundException;
import com.evolunteer.evm.common.mapper.VerificationTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final VerificationTokenMapper verificationTokenTransformer;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenConfig verificationTokenConfig;

    @Override
    public VerificationTokenDto create(final String token) {
        final VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(passwordEncoder.encode(token));
        verificationToken.setExpirationTime(LocalDateTime.now().plus(verificationTokenConfig.getVerificationTokenExpiration(),
                verificationTokenConfig.getChronoUnitType()));
        return verificationTokenTransformer.mapVerificationTokenToVerificationTokenDto(verificationTokenRepository.save(verificationToken));
    }

    @Override
    public TokenVerificationResult verifyToken(final Long verificationTokenId, final String token) {
        final String decodedToken;
        try {
            decodedToken = new String(Base64.getDecoder().decode(token));
        }catch (IllegalArgumentException e) {
            return TokenVerificationResult.INCORRECT_VERIFICATION_CREDENTIALS;
        }
        final VerificationToken verificationToken = verificationTokenRepository.findById(verificationTokenId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Verification token does not exist by id: %s", verificationTokenId)));
        if (verificationToken.isExpired()) {
            return TokenVerificationResult.EXPIRED_VERIFICATION_CREDENTIALS;
        }
        if (!passwordEncoder.matches(decodedToken, verificationToken.getToken())) {
            return TokenVerificationResult.INCORRECT_VERIFICATION_CREDENTIALS;
        }
        return TokenVerificationResult.SUCCESSFUL_VERIFICATION;
    }

    @Override
    public void deleteVerificationTokenById(final Long verificationTokenId) {
        final VerificationToken verificationToken = verificationTokenRepository.findById(verificationTokenId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Verification token does not exist by id: %s", verificationTokenId)));
        if (!verificationToken.isExpired()) {
            throw new IllegalStateException("Unable to delete not-expired verification token!");
        }
        verificationTokenRepository.deleteById(verificationTokenId);
    }
}
