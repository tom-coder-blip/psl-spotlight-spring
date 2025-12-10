package net.javaguides.pslspotlightspring.entities;

import java.time.Instant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    private String type; // "LIKE", "COMMENT", "FOLLOW"
    private String message;
    private boolean read = false;

    @CreationTimestamp
    private Instant createdAt;
}