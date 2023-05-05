package com.evolunteer.evm.common.domain.enums.user_management;

import org.springframework.security.core.GrantedAuthority;

/**
 * Date: 26.04.23
 *
 * @author ilia
 */
public enum AccountRole implements GrantedAuthority {
    ROLE_VOLUNTEER;

    @Override
    public String getAuthority() {
        return name();
    }
}
