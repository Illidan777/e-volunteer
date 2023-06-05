package com.evolunteer.evm.common.domain.enums.fund_management;

public enum FundRequestStatus {
    NEW, ACCEPTED, REJECTED;

    public boolean isNew() {
        return this.equals(NEW);
    }
}
