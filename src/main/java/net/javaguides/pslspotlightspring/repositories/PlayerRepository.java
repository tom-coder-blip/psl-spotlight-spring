package net.javaguides.pslspotlightspring.repositories;

import net.javaguides.pslspotlightspring.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByNameContainingIgnoreCaseOrClubContainingIgnoreCase(String nameKeyword, String clubKeyword);
    List<Player> findTop5ByOrderByTrendingRatingDesc();
}