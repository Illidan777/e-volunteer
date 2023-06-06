package com.evolunteer.evm.common.domain.enums.fund_management;

import lombok.Getter;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundProfileView.*;

@Getter
public enum FundHelpRequestStatus {
    NEW(FUND_HELP_REQUEST_STATUS_NEW, FUND_HELP_REQUEST_NOTIFICATION_STATUS_NEW, "badge"),
    ACCEPTED(FUND_HELP_REQUEST_STATUS_ACCEPTED, FUND_HELP_REQUEST_NOTIFICATION_STATUS_ACCEPTED, "badge contrast"),
    REJECTED(FUND_HELP_REQUEST_STATUS_REJECTED, FUND_HELP_REQUEST_NOTIFICATION_STATUS_REJECTED, "badge error"),
    IN_PROGRESS(FUND_HELP_REQUEST_STATUS_IN_PROGRESS, FUND_HELP_REQUEST_NOTIFICATION_STATUS_IN_PROGRESS, "badge"),
    COMPLETED(FUND_HELP_REQUEST_STATUS_COMPLETED, FUND_HELP_REQUEST_NOTIFICATION_STATUS_COMPLETED, "badge success");

    private final String localizedValue;
    private final String localizedMessage;
    private final String style;

    FundHelpRequestStatus(String localizedValue, String localizedMessage, String style) {
        this.localizedValue = localizedValue;
        this.localizedMessage = localizedMessage;
        this.style = style;
    }
}
