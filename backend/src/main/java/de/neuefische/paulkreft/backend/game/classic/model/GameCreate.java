package de.neuefische.paulkreft.backend.game.classic.model;

import java.time.Instant;
import java.util.List;

public record GameCreate(
        String userId,
        String type,
        int difficulty,
        boolean isSuccess,
        int duration,
        List<String> configuration
) {
    public ClassicGame withIdAndCreatedAt(String id, Instant createdAt) {
        return new ClassicGame(id, this.userId, this.type, this.difficulty, this.isSuccess, this.duration, this.configuration, createdAt);
    }
}
