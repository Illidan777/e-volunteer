package com.evolunteer.evm.common.domain.enums.user_management;

import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import lombok.Getter;

@Getter
public enum LinkVerificationResult {
    INCORRECT_VERIFICATION_CREDENTIALS(LocalizationUtils.Error.VALIDATION_INVALID_LINK_ERROR, true),
    EXPIRED_VERIFICATION_CREDENTIALS(LocalizationUtils.Error.VALIDATION_EXPIRED_VERIFICATION_LINK_ERROR, true),
    SUCCESSFUL_VERIFICATION(LocalizationUtils.UI.UserRegistrationDialog.SUCCESS_ACCOUNT_VERIFICATION, false);

    private final String localizedMessage;
    private final Boolean isError;

    LinkVerificationResult(String message, Boolean isError) {
        this.localizedMessage = message;
        this.isError = isError;
    }

    public boolean isSuccess() {
        return this.equals(SUCCESSFUL_VERIFICATION);
    }

    public boolean isExpired() {
        return this.equals(EXPIRED_VERIFICATION_CREDENTIALS);
    }
}
