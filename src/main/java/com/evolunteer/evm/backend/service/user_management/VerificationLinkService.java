package com.evolunteer.evm.backend.service.user_management;


import com.evolunteer.evm.common.domain.dto.user_management.VerificationLinkDto;
import com.evolunteer.evm.common.domain.enums.user_management.LinkVerificationResult;

public interface VerificationLinkService {

    VerificationLinkDto create(String token);

    LinkVerificationResult verifyLink(Long verificationTokenId, String token);

    void deleteVerificationTokenById(Long verificationTokenId);
}
