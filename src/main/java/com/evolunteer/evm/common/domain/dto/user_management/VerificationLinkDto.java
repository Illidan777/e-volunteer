package com.evolunteer.evm.common.domain.dto.user_management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VerificationLinkDto {
    private Long id;
    private String token;
    private LocalDateTime expirationTime;

    @JsonIgnore
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationTime);
    }
}
