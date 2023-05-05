package com.evolunteer.evm.common.domain.entity.notification_management;

import com.evolunteer.evm.common.domain.enums.notification_management.NotificationProviderType;
import com.evolunteer.evm.common.domain.enums.notification_management.SendStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 06.07.22
 *
 * @author ilia
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class MessageEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String message;

    private String subject;

    private Integer sendAttempt = 0;

    @Column(columnDefinition = "text")
    private String reportMessage;

    @Enumerated(value = EnumType.STRING)
    private SendStatus status;

    @Enumerated(value = EnumType.STRING)
    private NotificationProviderType type;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ElementCollection
    private Set<String> accounts = new HashSet<>();
}
