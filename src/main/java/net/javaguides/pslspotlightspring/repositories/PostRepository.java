package net.javaguides.pslspotlightspring.repositories;

import net.javaguides.pslspotlightspring.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMatchweek(int matchweek);
    List<Post> findByPlayerName(String playerName);
    List<Post> findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCase(String titleKeyword, String tagKeyword);
}