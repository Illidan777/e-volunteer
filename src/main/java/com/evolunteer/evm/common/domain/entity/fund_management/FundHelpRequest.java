package com.evolunteer.evm.common.domain.entity.fund_management;

import com.evolunteer.evm.common.domain.enums.fund_management.FundHelpRequestStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "funds_help_requests")
@EqualsAndHashCode(exclude = {"fund"})
@EntityListeners(AuditingEntityListener.class)
public class FundHelpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    @Column(columnDefinition = "text")
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(value = EnumType.STRING)
    private FundHelpRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private HelpRequestExecutor executor;

    @ManyToOne
    @JoinColumn(name = "fund_id")
    private Fund fund;
}
