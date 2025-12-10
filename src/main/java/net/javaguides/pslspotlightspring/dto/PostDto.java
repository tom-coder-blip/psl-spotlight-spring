package net.javaguides.pslspotlightspring.dto;

import java.time.Instant;
import java.util.Set;

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
) {}
