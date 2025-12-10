package net.javaguides.pslspotlightspring.services;

import net.javaguides.pslspotlightspring.entities.Player;
import net.javaguides.pslspotlightspring.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    public Player updatePlayerStats(Long id, Player updated) {
        Player player = getPlayerById(id);
        player.setGoals(updated.getGoals());
        player.setAssists(updated.getAssists());
        player.setAppearances(updated.getAppearances());
        player.setTrendingRating(updated.getTrendingRating());
        if (updated.getPlayerPicture() != null) {
            player.setPlayerPicture(updated.getPlayerPicture());
        }
        return playerRepository.save(player);
    }

    public void deletePlayer(Long id) {
        Player player = getPlayerById(id); // ensures player exists
        playerRepository.delete(player);
    }
}