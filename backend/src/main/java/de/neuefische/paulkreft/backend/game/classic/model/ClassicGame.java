package de.neuefische.paulkreft.backend.game.classic.model;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

public record ClassicGame(
        @Id
        String id,
        String userId,
        String type,
        int difficulty,
        boolean isSuccess,
        int duration,
        List<String> configuration,
        Instant createdAt
) {
}
