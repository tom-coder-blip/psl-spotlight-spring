package net.javaguides.pslspotlightspring.services;

import net.javaguides.pslspotlightspring.entities.Player;
import net.javaguides.pslspotlightspring.repositories.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrendingService {

    private final PlayerRepository playerRepository;

    public TrendingService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public void addPostImpact(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        player.setTrendingRating(player.getTrendingRating() + 10);
        playerRepository.save(player);
    }

    @Transactional
    public void removePostImpact(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        player.setTrendingRating(player.getTrendingRating() - 10);
        playerRepository.save(player);
    }

    @Transactional
    public void addCommentImpact(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        player.setTrendingRating(player.getTrendingRating() + 4);
        playerRepository.save(player);
    }

    @Transactional
    public void removeCommentImpact(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        player.setTrendingRating(player.getTrendingRating() - 4);
        playerRepository.save(player);
    }

    @Transactional
    public void addLikeImpact(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        player.setTrendingRating(player.getTrendingRating() + 2);
        playerRepository.save(player);
    }

    @Transactional
    public void removeLikeImpact(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        player.setTrendingRating(player.getTrendingRating() - 2);
        playerRepository.save(player);
    }

    // Nightly decay: reduce trendingRating by 5%
    @Transactional
    public void applyDecay() {
        List<Player> players = playerRepository.findAll();
        for (Player player : players) {
            double decayed = player.getTrendingRating() * 0.95; // reduce by 5%
            player.setTrendingRating(decayed);
            playerRepository.save(player);
        }
        System.out.println("Trending ratings decayed successfully");
    }
}
