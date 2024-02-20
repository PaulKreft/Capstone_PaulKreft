package de.neuefische.paulkreft.backend.user.model;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@With
public record User(
        @Id
        String id,
        String name,
        String email,
        String password,
        Instant lastActive,
        Instant createdAt
) {
}
