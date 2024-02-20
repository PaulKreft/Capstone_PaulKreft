package de.neuefische.paulkreft.backend.security.model;

import jakarta.validation.constraints.Email;


public record SignUpRequest(
        @Email
        String email,
        String password
) {
}
