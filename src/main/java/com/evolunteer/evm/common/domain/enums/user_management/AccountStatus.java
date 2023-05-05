package com.evolunteer.evm.common.domain.enums.user_management;

public enum AccountStatus {
    NOT_VERIFIED, VERIFIED, EXPIRED;

    public boolean isExpired() {
        return this.equals(EXPIRED);
    }

    public boolean isNotVerified() {
        return this.equals(NOT_VERIFIED);
    }

    public boolean isVerified() {
        return this.equals(VERIFIED);
    }
}
