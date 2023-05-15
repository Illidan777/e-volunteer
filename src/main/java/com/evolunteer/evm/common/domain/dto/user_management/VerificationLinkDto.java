package com.evolunteer.evm.common.domain.dto.user_management;

import com.evolunteer.evm.common.domain.enums.user_management.VerificationLinkType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VerificationLinkDto {
    private Long id;
    private String token;
    private LocalDateTime expirationTime;
    private VerificationLinkType type;

    @JsonIgnore
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationTime);
    }
}
