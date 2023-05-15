package com.evolunteer.evm.common.domain.dto.user_management;

import com.evolunteer.evm.common.domain.enums.user_management.AccountRole;
import com.evolunteer.evm.common.domain.enums.user_management.AccountStatus;
import com.evolunteer.evm.common.domain.enums.user_management.AccountAuthType;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class AccountDto {
    private Long id;
    private String username;
    private String password;
    private AccountStatus status;
    private AccountAuthType authType;
    private Set<AccountRole> roles = new HashSet<>();
}
