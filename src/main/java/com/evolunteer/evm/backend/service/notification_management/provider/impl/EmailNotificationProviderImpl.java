package com.evolunteer.evm.backend.service.notification_management.provider.impl;

import com.evolunteer.evm.backend.service.notification_management.provider.NotificationProvider;
import com.evolunteer.evm.common.domain.dto.notification_management.NotificationData;
import com.evolunteer.evm.common.domain.exception.notification_management.NotificationSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Date: 06.07.22
 *
 * @author ilia
 */
@Slf4j
@Component(value = "email-notification-provider")
@RequiredArgsConstructor
public class EmailNotificationProviderImpl implements NotificationProvider {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void notify(NotificationData notificationData) throws NotificationSendingException {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(notificationData.getAccounts().toArray(new String[0]));
        message.setSubject(notificationData.getSubject());
        message.setText(notificationData.getMessage());

        try {
             javaMailSender.send(message);
        } catch (Exception cause) {
            log.error("Sending email notification exception!");
            throw new NotificationSendingException(cause);
        }
    }
}
