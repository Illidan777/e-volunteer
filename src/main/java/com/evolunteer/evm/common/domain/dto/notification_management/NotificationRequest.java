package com.evolunteer.evm.common.domain.dto.notification_management;

import com.evolunteer.evm.common.domain.enums.notification_management.NotificationProviderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 06.07.22
 *
 * @author ilia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class NotificationRequest implements NotificationData {
    @NotBlank(message = "Message can not blank")
    private String message;
    @NotBlank(message = "Subject can not blank")
    private String subject;
    @NotNull(message = "Notifier type can not be null")
    private NotificationProviderType notificationProviderType = NotificationProviderType.EMAIL;
    @NotNull(message = "Accounts type can not be null")
    private Set<String> accounts = new HashSet<>();
}
