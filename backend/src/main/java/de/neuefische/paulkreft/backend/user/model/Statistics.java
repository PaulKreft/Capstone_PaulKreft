package de.neuefische.paulkreft.backend.user.model;

public record Statistics(
        ScoreMap longestWinningStreak,
        ScoreMap longestLosingStreak,
        ScoreMap gamesPlayed,
        ScoreMap gamesWon,
        ScoreMap fastestSolve,
        ScoreMap averageTime
) {
}
