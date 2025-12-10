package net.javaguides.pslspotlightspring.repositories;
import net.javaguides.pslspotlightspring.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
    List<Like> findByPostId(Long postId);
}
