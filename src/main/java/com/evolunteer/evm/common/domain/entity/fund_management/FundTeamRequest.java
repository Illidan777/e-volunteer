package com.evolunteer.evm.common.domain.entity.fund_management;

import com.evolunteer.evm.common.domain.entity.user_management.User;
import com.evolunteer.evm.common.domain.enums.fund_management.FundRequestStatus;
import com.evolunteer.evm.common.domain.enums.fund_management.FundRequestType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@Table(name = "funds_requests")
@EqualsAndHashCode(exclude = {"fund", "user"})
public class FundTeamRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private FundRequestType fundRequestType;

    @Enumerated(value = EnumType.STRING)
    private FundRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fund_id")
    private Fund fund;
}
