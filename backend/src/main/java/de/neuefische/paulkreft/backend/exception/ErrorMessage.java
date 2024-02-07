package de.neuefische.paulkreft.backend.exception;

import java.time.Instant;

public record ErrorMessage(
        String message,
        Instant timestamp
) {
    public ErrorMessage(String message) {
        this(message, Instant.now());
    }
}