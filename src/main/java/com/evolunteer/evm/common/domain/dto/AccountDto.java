package com.evolunteer.evm.common.domain.dto;

import com.evolunteer.evm.common.domain.enums.AccountRole;
import com.evolunteer.evm.common.domain.enums.AccountStatus;
import com.evolunteer.evm.common.domain.enums.AccountType;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class AccountDto {
    private Long id;
    private String username;
    private String password;
    private AccountStatus status;
    private AccountType type;
    private Set<AccountRole> roles = new HashSet<>();
}
