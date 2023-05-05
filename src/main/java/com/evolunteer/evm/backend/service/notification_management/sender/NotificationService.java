package com.evolunteer.evm.backend.service.notification_management.sender;

import com.evolunteer.evm.common.domain.dto.notification_management.NotificationData;

public interface NotificationService {

    void send(NotificationData notificationRequest);
}
