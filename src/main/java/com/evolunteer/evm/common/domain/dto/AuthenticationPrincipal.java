package com.evolunteer.evm.common.domain.dto;

import com.evolunteer.evm.common.domain.entity.Account;
import com.evolunteer.evm.common.domain.enums.AccountStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Data
@RequiredArgsConstructor
public class AuthenticationPrincipal implements UserDetails {

    private Account account;

    public AuthenticationPrincipal(Account account) {
        this.account = Objects.requireNonNull(account);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getRoles();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.getStatus().equals(AccountStatus.VERIFIED);
    }
}
