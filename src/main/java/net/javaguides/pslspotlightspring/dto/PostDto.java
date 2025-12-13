package net.javaguides.pslspotlightspring.dto;

import java.time.Instant;
import java.util.Set;
import net.javaguides.pslspotlightspring.entities.Post;

public record PostDto(
        Long id,
        String playerName,
        String club,
        int matchweek,
        String title,
        String content,
        String image,
        String username,
        Set<String> tags,
        int likeCount,
        Instant createdAt
) {
    public static PostDto from(Post post) {
        return new PostDto(
                post.getId(),
                post.getPlayerName(),
                post.getClub(),
                post.getMatchweek(),
                post.getTitle(),
                post.getContent(),
                post.getImage(),
                post.getUser().getUsername(),
                post.getTags(),
                post.getLikes().size(),
                post.getCreatedAt()
        );
    }
}
