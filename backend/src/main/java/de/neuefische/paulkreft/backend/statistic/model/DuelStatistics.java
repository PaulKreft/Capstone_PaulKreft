package de.neuefische.paulkreft.backend.statistic.model;

public record DuelStatistics(
        String player,
        String opponent,
        ScoreMap gamesPlayed,
        ScoreMap gamesWon
) {
}
