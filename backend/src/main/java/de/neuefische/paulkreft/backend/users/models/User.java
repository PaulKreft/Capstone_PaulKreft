package de.neuefische.paulkreft.backend.users.models;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@With
public record User(
        @Id
        String id,
        String name,
        String email,
        Instant lastActive,
        Instant createdAt
) {
}
