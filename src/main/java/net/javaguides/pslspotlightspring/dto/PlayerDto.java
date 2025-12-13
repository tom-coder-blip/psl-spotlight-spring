package net.javaguides.pslspotlightspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class PlayerDto {
    private Long id;
    private String name;
    private String club;
    private String position;
    private String playerPicture;
    private int goals;
    private int assists;
    private int appearances;
    private double trendingRating;
    private Instant createdAt;
}