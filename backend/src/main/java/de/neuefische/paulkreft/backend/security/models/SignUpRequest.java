package de.neuefische.paulkreft.backend.security.models;

import jakarta.validation.constraints.Email;


public record SignUpRequest(
        @Email
        String email,
        String password
) {
}
