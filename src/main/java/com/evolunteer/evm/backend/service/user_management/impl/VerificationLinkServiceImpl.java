package com.evolunteer.evm.backend.service.user_management.impl;

import com.evolunteer.evm.backend.repository.user_management.VerificationTokenRepository;
import com.evolunteer.evm.backend.security.config.VerificationLinkConfig;
import com.evolunteer.evm.backend.service.user_management.VerificationLinkService;
import com.evolunteer.evm.common.domain.dto.user_management.VerificationLinkDto;
import com.evolunteer.evm.common.domain.entity.user_management.VerificationLink;
import com.evolunteer.evm.common.domain.enums.user_management.LinkVerificationResult;
import com.evolunteer.evm.common.domain.enums.user_management.VerificationLinkType;
import com.evolunteer.evm.common.domain.exception.common.ResourceNotFoundException;
import com.evolunteer.evm.common.mapper.VerificationLinkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class VerificationLinkServiceImpl implements VerificationLinkService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final VerificationLinkMapper verificationTokenTransformer;
    private final PasswordEncoder passwordEncoder;
    private final VerificationLinkConfig verificationLinkConfig;

    @Override
    public VerificationLinkDto create(final String token, final VerificationLinkType type) {
        final VerificationLink verificationLink = new VerificationLink();
        verificationLink.setToken(passwordEncoder.encode(token));
        verificationLink.setExpirationTime(LocalDateTime.now().plus(verificationLinkConfig.getVerificationTokenExpiration(),
                verificationLinkConfig.getChronoUnitType()));
        verificationLink.setType(type);
        return verificationTokenTransformer.mapVerificationLinkToVerificationLinkDto(verificationTokenRepository.save(verificationLink));
    }

    @Override
    public LinkVerificationResult verifyLink(final Long verificationTokenId, final String token) {
        final String decodedToken;
        try {
            decodedToken = new String(Base64.getDecoder().decode(token));
        }catch (IllegalArgumentException e) {
            return LinkVerificationResult.INCORRECT_VERIFICATION_CREDENTIALS;
        }
        final VerificationLink verificationLink = verificationTokenRepository.findById(verificationTokenId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Verification token does not exist by id: %s", verificationTokenId)));
        if (verificationLink.isExpired()) {
            return LinkVerificationResult.EXPIRED_VERIFICATION_CREDENTIALS;
        }
        if (!passwordEncoder.matches(decodedToken, verificationLink.getToken())) {
            return LinkVerificationResult.INCORRECT_VERIFICATION_CREDENTIALS;
        }
        return LinkVerificationResult.SUCCESSFUL_VERIFICATION;
    }

    @Override
    public void deleteVerificationTokenById(final Long verificationTokenId) {
        final VerificationLink verificationLink = verificationTokenRepository.findById(verificationTokenId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Verification token does not exist by id: %s", verificationTokenId)));
        if (!verificationLink.isExpired()) {
            throw new IllegalStateException("Unable to delete not-expired verification token!");
        }
        verificationTokenRepository.deleteById(verificationTokenId);
    }
}
