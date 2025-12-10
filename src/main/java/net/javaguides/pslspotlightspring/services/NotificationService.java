package net.javaguides.pslspotlightspring.services;

import net.javaguides.pslspotlightspring.entities.Notification;
import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.repositories.NotificationRepository;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import net.javaguides.pslspotlightspring.dto.NotificationDto;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void sendNotification(Long recipientId, String type, String message) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(type)
                .message(message)
                .build();

        notificationRepository.save(notification);
    }

    public List<NotificationDto> getUserNotifications(Long userId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationDto::from)
                .toList();
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
