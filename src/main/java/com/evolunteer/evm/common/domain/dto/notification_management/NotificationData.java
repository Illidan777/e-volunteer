package com.evolunteer.evm.common.domain.dto.notification_management;


import com.evolunteer.evm.common.domain.enums.notification_management.NotificationProviderType;

import java.util.Set;

/**
 * Date: 06.07.22
 *
 * @author ilia
 */
public interface NotificationData {

    String getMessage();

    String getSubject();

    NotificationProviderType getNotificationProviderType();

    Set<String> getAccounts();
}
