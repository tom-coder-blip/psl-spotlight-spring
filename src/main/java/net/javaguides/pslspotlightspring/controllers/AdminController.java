package net.javaguides.pslspotlightspring.controllers;

import net.javaguides.pslspotlightspring.entities.Player;
import net.javaguides.pslspotlightspring.entities.Post;
import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.repositories.PlayerRepository;
import net.javaguides.pslspotlightspring.repositories.PostRepository;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final PlayerRepository playerRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public AdminController(PlayerRepository playerRepository, PostRepository postRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/top-players")
    public ResponseEntity<List<Player>> getTopPlayers() {
        return ResponseEntity.ok(playerRepository.findTop5ByOrderByTrendingRatingDesc());
    }

    @GetMapping("/top-posts")
    public ResponseEntity<List<Post>> getTopPosts() {
        return ResponseEntity.ok(postRepository.findTop5ByOrderByLikesDesc());
    }

    @GetMapping("/top-users")
    public ResponseEntity<List<User>> getTopUsers() {
        return ResponseEntity.ok(userRepository.findTop5ByOrderByFollowersDesc());
    }
}