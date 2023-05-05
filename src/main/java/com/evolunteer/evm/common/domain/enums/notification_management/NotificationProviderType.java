package com.evolunteer.evm.common.domain.enums.notification_management;

import lombok.Getter;

/**
 * Date: 06.07.22
 *
 * @author ilia
 */
@Getter
public enum NotificationProviderType {
    EMAIL("email-notification-provider"),
    EMAIL_WITH_ATTACHMENTS("email-with-attachment-notification-provider");

    NotificationProviderType(String provider) {
        this.provider = provider;
    }

    private final String provider;
}
