package com.evolunteer.evm.backend.service.notification_management.factory;

import com.evolunteer.evm.common.domain.dto.notification_management.NotificationData;
import com.evolunteer.evm.common.domain.entity.notification_management.MessageEntry;
import com.evolunteer.evm.common.domain.enums.notification_management.SendStatus;

/**
 * Date: 06.07.22
 *
 * @author ilia
 */
public class MessageEntryFactory {

    private static final Integer INITIAL_SEND_ATTEMPT_VALUE = 1;
    private static final String DEFAULT_REPORT_MESSAGE = "OK!";

    public static MessageEntry invalidRequest(NotificationData notificationRequest, String message) {
        return MessageEntry.builder()
                .accounts(notificationRequest.getAccounts())
                .status(SendStatus.INVALID_REQUEST)
                .message(notificationRequest.getMessage())
                .subject(notificationRequest.getSubject())
                .sendAttempt(INITIAL_SEND_ATTEMPT_VALUE)
                .reportMessage(message)
                .type(notificationRequest.getNotificationProviderType())
                .build();
    }

    public static MessageEntry buildDefault(NotificationData notificationRequest) {
        return MessageEntry.builder()
                .accounts(notificationRequest.getAccounts())
                .status(SendStatus.IN_PROGRESS)
                .message(notificationRequest.getMessage())
                .subject(notificationRequest.getSubject())
                .reportMessage(DEFAULT_REPORT_MESSAGE)
                .sendAttempt(INITIAL_SEND_ATTEMPT_VALUE)
                .type(notificationRequest.getNotificationProviderType())
                .build();
    }

    public static MessageEntry error(NotificationData notificationRequest, String errorMessage, Integer sendAttempt) {
        return MessageEntry.builder()
                .accounts(notificationRequest.getAccounts())
                .status(SendStatus.ERROR)
                .message(notificationRequest.getMessage())
                .subject(notificationRequest.getSubject())
                .reportMessage(errorMessage)
                .sendAttempt(sendAttempt)
                .type(notificationRequest.getNotificationProviderType())
                .build();
    }

    public static MessageEntry success(NotificationData notificationRequest, Integer sendAttempt) {
        return MessageEntry.builder()
                .accounts(notificationRequest.getAccounts())
                .status(SendStatus.SUCCESS)
                .message(notificationRequest.getMessage())
                .subject(notificationRequest.getSubject())
                .reportMessage(DEFAULT_REPORT_MESSAGE)
                .sendAttempt(sendAttempt)
                .type(notificationRequest.getNotificationProviderType())
                .build();
    }
}
