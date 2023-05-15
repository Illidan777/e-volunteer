package com.evolunteer.evm.common.domain.entity.user_management;

import com.evolunteer.evm.common.domain.enums.user_management.VerificationLinkType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "verification_links")
public class VerificationLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expirationTime;

    @Enumerated(value = EnumType.STRING)
    private VerificationLinkType type;


    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationTime);
    }
}
