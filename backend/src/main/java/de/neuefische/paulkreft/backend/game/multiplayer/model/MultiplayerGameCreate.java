package de.neuefische.paulkreft.backend.game.multiplayer.model;

import java.time.Instant;
import java.util.List;

public record MultiplayerGameCreate(
        List<String> playerIds,
        int difficulty,
        Integer streakToWin,
        List<String> winnerIds,
        List<String> loserIds,
        Integer wonInMilliseconds,
        int totalPlayers
) {
    public MultiplayerGame withIdAndCreatedAt(String id, Instant createdAt) {
        return new MultiplayerGame(id, this.playerIds, this.difficulty, this.streakToWin, this.winnerIds, this.loserIds, this.wonInMilliseconds, this.totalPlayers , createdAt);
    }
}
