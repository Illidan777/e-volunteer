package com.evolunteer.evm.common.domain.enums.fund_management;

public enum FundRequestType {

    FUND_INVITATION, USER_REQUEST;
    
    public boolean isFundInvitation() {
        return this.equals(FUND_INVITATION);
    }

    public boolean isUserRequest() {
        return this.equals(USER_REQUEST);
    }
}
