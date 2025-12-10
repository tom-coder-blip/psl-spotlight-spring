package net.javaguides.pslspotlightspring.dto;

import java.time.Instant;

public record CommentDto(
        Long id,
        String content,
        String username,
        Instant createdAt
) {}