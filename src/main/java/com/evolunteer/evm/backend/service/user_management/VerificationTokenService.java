package com.evolunteer.evm.backend.service.user_management;


import com.evolunteer.evm.common.domain.dto.user_management.VerificationTokenDto;
import com.evolunteer.evm.common.domain.enums.user_management.TokenVerificationResult;

public interface VerificationTokenService {

    VerificationTokenDto create(String token);

    TokenVerificationResult verifyToken(Long verificationTokenId, String token);

    void deleteVerificationTokenById(Long verificationTokenId);
}
