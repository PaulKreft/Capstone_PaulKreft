package de.neuefische.paulkreft.backend.game.multiplayer.model;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

public record MultiplayerGame(
        @Id
        String id,
        List<String> playerIds,
        int difficulty,
        Integer streakToWin,
        List<String> winnerIds,
        List<String> loserIds,
        Integer wonInMilliseconds,
        int totalPlayers,
        Instant createdAt
) {
}
