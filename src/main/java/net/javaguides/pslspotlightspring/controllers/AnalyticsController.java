package net.javaguides.pslspotlightspring.controllers;

import net.javaguides.pslspotlightspring.repositories.PlayerRepository;
import net.javaguides.pslspotlightspring.repositories.PostRepository;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private final PlayerRepository playerRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public AnalyticsController(PlayerRepository playerRepository,
                               PostRepository postRepository,
                               UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Summary stats
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalPlayers", playerRepository.count());
        summary.put("totalPosts", postRepository.count());
        summary.put("totalUsers", userRepository.count());
        return ResponseEntity.ok(summary);
    }

    // Trending players
    @GetMapping("/trending-players")
    public ResponseEntity<?> getTrendingPlayers() {
        return ResponseEntity.ok(playerRepository.findTop5ByOrderByTrendingRatingDesc());
    }

    // 🏟️ Post volume by week (optional if you track matchweek in Post)
    @GetMapping("/posts-by-week")
    public ResponseEntity<Map<Integer, Long>> getPostsByWeek() {
        Map<Integer, Long> postsByWeek = new HashMap<>();
        postRepository.findAll().forEach(post -> {
            Integer week = post.getMatchweek();
            postsByWeek.put(week, postsByWeek.getOrDefault(week, 0L) + 1);
        });
        return ResponseEntity.ok(postsByWeek);
    }

    // 👥 User growth (optional: if you track createdAt in User)
    @GetMapping("/user-growth")
    public ResponseEntity<Long> getUserGrowth() {
        return ResponseEntity.ok(userRepository.count()); // simple version
    }
}