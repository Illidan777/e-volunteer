package com.evolunteer.evm.common.domain.entity;

import com.evolunteer.evm.common.domain.enums.AccountRole;
import com.evolunteer.evm.common.domain.enums.AccountStatus;
import com.evolunteer.evm.common.domain.enums.AccountType;
import lombok.Data;

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
    private AccountType type;

    @ElementCollection(targetClass = AccountRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id"))
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles = new HashSet<>();
}
