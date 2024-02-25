package de.neuefische.paulkreft.backend.statistic.model;

public record ClassicStatistics(
        ScoreMap longestWinningStreak,
        ScoreMap longestLosingStreak,
        ScoreMap gamesPlayed,
        ScoreMap gamesWon,
        ScoreMap fastestSolve,
        ScoreMap averageTime
) {
}
