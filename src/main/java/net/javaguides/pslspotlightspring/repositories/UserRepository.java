package net.javaguides.pslspotlightspring.repositories;

import net.javaguides.pslspotlightspring.entities.Player;
import net.javaguides.pslspotlightspring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findByUsernameContainingIgnoreCaseOrTeamSupportedContainingIgnoreCase(String usernameKeyword, String teamKeyword);
}