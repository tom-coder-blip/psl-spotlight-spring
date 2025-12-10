package net.javaguides.pslspotlightspring.dto;

import lombok.Getter;
import lombok.Setter;
import net.javaguides.pslspotlightspring.entities.Notification;
import java.time.Instant;

@Getter @Setter
public class NotificationDto {
    private Long id;
    private String type;
    private String message;
    private boolean read;
    private Instant createdAt;

    public static NotificationDto from(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}