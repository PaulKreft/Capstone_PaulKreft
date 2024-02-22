package de.neuefische.paulkreft.backend.game.multiplayer.model;

import java.time.Instant;
import java.util.List;

public record MultiplayerGameCreate(
        List<String> playerIds,
        int difficulty,
        Integer streakToWin,
        String winnerId,
        List<String> loserIds,
        Integer wonInMilliseconds
) {
    public MultiplayerGame withIdAndCreatedAt(String id, Instant createdAt) {
        return new MultiplayerGame(id, this.playerIds, this.difficulty, this.streakToWin, this.winnerId, this.loserIds, this.wonInMilliseconds, createdAt);
    }
}
