package de.neuefische.paulkreft.backend.security.models;

public record SignUpRequest(
        String email,
        String password
) {
}
