package net.javaguides.pslspotlightspring.dto;


import java.time.Instant;

public record LikeDto(
        Long id,
        String username,
        Instant createdAt
) {}