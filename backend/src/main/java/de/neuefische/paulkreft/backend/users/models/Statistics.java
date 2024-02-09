package de.neuefische.paulkreft.backend.users.models;

public record Statistics(
        ScoreMap longestWinningStreak,
        ScoreMap longestLosingStreak,
        ScoreMap gamesPlayed,
        ScoreMap gamesWon,
        ScoreMap fastestSolve,
        ScoreMap averageTime
) {
}
