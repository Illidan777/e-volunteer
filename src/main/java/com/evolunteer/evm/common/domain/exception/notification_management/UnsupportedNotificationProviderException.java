package com.evolunteer.evm.common.domain.exception.notification_management;

/**
 * Date: 06.07.22
 *
 * @author ilia
 */
public class UnsupportedNotificationProviderException extends RuntimeException {

    public UnsupportedNotificationProviderException(String message) {
        super(message);
    }
}
