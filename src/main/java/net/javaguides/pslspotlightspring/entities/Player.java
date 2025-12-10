package net.javaguides.pslspotlightspring.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "players")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    private String playerPicture;
    private String club;
    private String position;

    private int goals = 0;
    private int assists = 0;
    private int appearances = 0;

    private double trendingRating = 0.0;

    @CreationTimestamp
    private Instant createdAt;
}
