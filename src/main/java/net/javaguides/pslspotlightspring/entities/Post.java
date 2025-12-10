package net.javaguides.pslspotlightspring.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable=false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    // Link to Player
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable=false)
    private Player player;

    @Column(nullable=false)
    private String playerName;

    @Column(nullable=false)
    private String club;

    @Column(nullable=false)
    private int matchweek;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false, length=5000)
    private String content;

    private String image;

    @ElementCollection
    private Set<String> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private Set<User> likes = new HashSet<>();

    @CreationTimestamp
    private Instant createdAt;
}
