package com.evolunteer.evm.common.domain.entity.user_management;

import com.evolunteer.evm.common.domain.enums.user_management.AccountRole;
import com.evolunteer.evm.common.domain.enums.user_management.AccountStatus;
import com.evolunteer.evm.common.domain.enums.user_management.AccountAuthType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 26.04.23
 *
 * @author ilia
 */
@Entity
@Data
@Table(name = "accounts")
@EqualsAndHashCode(exclude = "user")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    @Enumerated(value = EnumType.STRING)
    private AccountAuthType authType;

    @ElementCollection(targetClass = AccountRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id"))
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "verification_link_id")
    private VerificationLink verificationLink;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passowrd_recover_link_id")
    private VerificationLink passwordRecoverLink;

    @OneToOne(mappedBy = "accountDetails")
    private User user;
}
