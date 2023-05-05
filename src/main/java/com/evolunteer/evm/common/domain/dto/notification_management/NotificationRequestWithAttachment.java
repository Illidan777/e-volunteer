package com.evolunteer.evm.common.domain.dto.notification_management;

import com.evolunteer.evm.common.domain.enums.notification_management.NotificationAttachmentType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NotificationRequestWithAttachment extends NotificationRequest {
    private Map<NotificationAttachmentType, Map<String, byte[]>> attachments = new HashMap<>();
}
