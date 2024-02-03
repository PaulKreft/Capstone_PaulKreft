package de.neuefische.paulkreft.backend.users.models;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@With
public record User(
        @Id
        String id,
        int githubId,
        String name,
        Instant lastActive,
        Instant createdAt
) {
}
