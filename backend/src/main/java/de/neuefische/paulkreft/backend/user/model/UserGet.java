package de.neuefische.paulkreft.backend.user.model;

import org.springframework.data.annotation.Id;

import java.time.Instant;

public record UserGet(
        @Id
        String id,
        String name,
        String email,
        Instant lastActive,
        Instant createdAt
) {

    public UserGet(User user) {
        this(user.id(), user.name(), user.email(), user.lastActive(), user.createdAt());
    }
}
