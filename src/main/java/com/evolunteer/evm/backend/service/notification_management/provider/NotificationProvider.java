package com.evolunteer.evm.backend.service.notification_management.provider;


import com.evolunteer.evm.common.domain.dto.notification_management.NotificationData;
import com.evolunteer.evm.common.domain.exception.notification_management.NotificationSendingException;

/**
 * Date: 06.07.22
 *
 * @author ilia
 */
public interface NotificationProvider {

    void notify(NotificationData notificationData) throws NotificationSendingException;
}
