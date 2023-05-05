package com.evolunteer.evm.backend.service.notification_management.provider.impl;

import com.evolunteer.evm.backend.service.notification_management.provider.NotificationProvider;
import com.evolunteer.evm.common.domain.dto.notification_management.NotificationData;
import com.evolunteer.evm.common.domain.dto.notification_management.NotificationRequestWithAttachment;
import com.evolunteer.evm.common.domain.exception.notification_management.NotificationBuildingException;
import com.evolunteer.evm.common.domain.exception.notification_management.NotificationSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Slf4j
@Component(value = "email-with-attachment-notification-provider")
@RequiredArgsConstructor
public class EmailWithAttachmentNotificationProvider implements NotificationProvider {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void notify(NotificationData notificationData) throws NotificationSendingException {

        final MimeMessage message = null;
        final MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
        } catch (MessagingException e) {
            throw new NotificationBuildingException("An occurred exception during building notification with " +
                    "attachments attachment to notification!", e);
        }
        final NotificationRequestWithAttachment requestWithAttachment = ((NotificationRequestWithAttachment) notificationData);

        try {
            helper.setFrom(from);
            helper.setTo(requestWithAttachment.getAccounts().toArray(new String[0]));
            helper.setSubject(requestWithAttachment.getSubject());
            helper.setText(requestWithAttachment.getMessage());
            setAttachments(helper, requestWithAttachment);
        } catch (MessagingException e) {
            throw new NotificationBuildingException("An occurred exception during building notification with " +
                    "attachments attachment to notification!", e);
        }
        try {
            //javaMailSender.send(message);
        } catch (Exception cause) {
            log.error("Sending email notification exception!");
            throw new NotificationSendingException(cause);
        }
    }

    private static void setAttachments(MimeMessageHelper helper, NotificationRequestWithAttachment requestWithAttachment) {
        requestWithAttachment.getAttachments().forEach((notificationAttachmentType, attachmentMap) ->
                attachmentMap.forEach((fileName, attachment) -> {
                    try {
                        helper.addAttachment(fileName, new ByteArrayResource(attachment), notificationAttachmentType.getContentType());
                    } catch (MessagingException e) {
                        throw new NotificationBuildingException("An occurred exception during adding attachment to notification!", e);
                    }
                }));
    }
}
