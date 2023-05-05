package com.evolunteer.evm.backend.service.notification_management.sender.impl;

import com.evolunteer.evm.backend.repository.notification_management.MessageEntryRepository;
import com.evolunteer.evm.backend.service.notification_management.factory.MessageEntryFactory;
import com.evolunteer.evm.backend.service.notification_management.sender.NotificationService;
import com.evolunteer.evm.backend.service.notification_management.provider.NotificationProvider;
import com.evolunteer.evm.common.domain.dto.notification_management.NotificationData;
import com.evolunteer.evm.common.domain.entity.notification_management.MessageEntry;
import com.evolunteer.evm.common.domain.enums.notification_management.NotificationProviderType;
import com.evolunteer.evm.common.domain.exception.notification_management.NotificationSendingException;
import com.evolunteer.evm.common.domain.exception.notification_management.UnsupportedNotificationProviderException;
import com.evolunteer.evm.common.domain.exception.validation.ValidationException;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final MessageEntryRepository messageEntryRepository;
    private final ApplicationContext applicationContext;

    @Value("${default.retry.send.attempts.count}")
    private Integer defaultRetrySendAttemptsCount;

    @Override
    public void send(NotificationData notificationRequest) {
        log.info("Start of processing notification. Request: {}", notificationRequest.toString());
        MessageEntry messageEntry = MessageEntryFactory.buildDefault(notificationRequest);
        try {
            ValidationUtils.validate(notificationRequest);
        } catch (ValidationException cause) {
            log.error("An occurred validation exception due to sending notification. Invalid request!", cause);
            messageEntry = MessageEntryFactory.invalidRequest(notificationRequest, cause.getLocalizedMessage());
            messageEntryRepository.save(messageEntry);
            return;
        }

        final NotificationProviderType notificationProviderType = notificationRequest.getNotificationProviderType();

        NotificationProvider provider;
        try {
            provider = this.getNotificationProvider(notificationProviderType);
        } catch (UnsupportedNotificationProviderException cause) {
            log.error("An occurred unsupported notification provider exception due to sending notification." +
                    " Unsupported notifier type! Notifier type: " + notificationProviderType, cause);
            messageEntry = MessageEntryFactory.invalidRequest(notificationRequest, cause.getLocalizedMessage());
            messageEntryRepository.save(messageEntry);
            return;
        }

        Integer currentSendAttempt = 1;
        while (currentSendAttempt <= defaultRetrySendAttemptsCount) {
            try {
                provider.notify(notificationRequest);
                messageEntry = MessageEntryFactory.success(notificationRequest, currentSendAttempt);
                break;
            } catch (NotificationSendingException cause) {
                log.error("An occurred exception due to sending notification. Notification was not sent! " +
                        "Current attempt: " + currentSendAttempt, cause);
                currentSendAttempt++;
                messageEntry = MessageEntryFactory.error(notificationRequest, cause.getLocalizedMessage(), currentSendAttempt);
            }
        }

        messageEntryRepository.save(messageEntry);
        log.info("Notification has been processed. Status: {}. Report: {}.", messageEntry.getStatus(),
                messageEntry.getReportMessage());
    }

    private NotificationProvider getNotificationProvider(NotificationProviderType notificationProviderType) throws UnsupportedNotificationProviderException {
        final NotificationProvider gateway;
        try {
            gateway = (NotificationProvider) applicationContext.getBean(notificationProviderType.getProvider());
        } catch (BeansException cause) {
            throw new UnsupportedNotificationProviderException(cause.getLocalizedMessage());
        }
        return gateway;
    }
}
