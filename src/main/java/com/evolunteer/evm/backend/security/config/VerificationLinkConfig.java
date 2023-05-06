package com.evolunteer.evm.backend.security.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.temporal.ChronoUnit;

@Data
@Configuration
public class VerificationLinkConfig {

    @Value("${verification.token.length}")
    private Integer verificationTokenLength;

    @Value("${verification.token.expiration}")
    private Integer verificationTokenExpiration;

    @Value("${verification.token.expiration.time.unit.type}")
    private String verificationTokenExpirationTimeUnitType;

    public ChronoUnit getChronoUnitType() {
        if(StringUtils.isBlank(this.verificationTokenExpirationTimeUnitType)) {
            throw new IllegalArgumentException("Verification token time unit type should be specified");
        }
        return ChronoUnit.valueOf(this.verificationTokenExpirationTimeUnitType);
    }
}
