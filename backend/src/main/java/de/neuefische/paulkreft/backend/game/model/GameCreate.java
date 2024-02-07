package de.neuefische.paulkreft.backend.game.model;

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

    public Game withIdAndCreatedAt(String id, Instant createdAt) {
        return new Game(id, this.userId, this.type, this.difficulty, this.isSuccess, this.duration, this.configuration, createdAt);
    }
}
