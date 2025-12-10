package net.javaguides.pslspotlightspring.controllers;

import net.javaguides.pslspotlightspring.entities.Player;
import net.javaguides.pslspotlightspring.entities.Post;
import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.repositories.PlayerRepository;
import net.javaguides.pslspotlightspring.repositories.PostRepository;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final PlayerRepository playerRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public SearchController(PlayerRepository playerRepository, PostRepository postRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> searchPlayers(@RequestParam String keyword) {
        List<Player> results = playerRepository.findByNameContainingIgnoreCaseOrClubContainingIgnoreCase(keyword, keyword);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String keyword) {
        List<Post> results = postRepository.findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCase(keyword, keyword);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> results = userRepository.findByUsernameContainingIgnoreCaseOrTeamSupportedContainingIgnoreCase(keyword, keyword);
        return ResponseEntity.ok(results);
    }
}