package net.javaguides.pslspotlightspring.dto;

import net.javaguides.pslspotlightspring.entities.Like;
import java.time.Instant;

public record LikeDto(
        Long id,
        String username,
        Instant createdAt
) {
    public static LikeDto from(Like like) {
        return new LikeDto(
                like.getId(),
                like.getUser().getUsername(),
                like.getCreatedAt()
        );
    }
}