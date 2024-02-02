package de.neuefische.paulkreft.backend.users.models;

import org.springframework.data.annotation.Id;

public record User(
        @Id
        String id,
        int githubId,
        String name
) {
}
