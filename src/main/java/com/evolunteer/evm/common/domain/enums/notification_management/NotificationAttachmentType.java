package com.evolunteer.evm.common.domain.enums.notification_management;

import lombok.Getter;

@Getter
public enum NotificationAttachmentType {
    PDF("application/pdf;charset=utf-8");

    private final String contentType;

    NotificationAttachmentType(String contentType) {
        this.contentType = contentType;
    }
}
