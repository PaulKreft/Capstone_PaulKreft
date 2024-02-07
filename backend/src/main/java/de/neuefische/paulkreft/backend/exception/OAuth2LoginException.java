package de.neuefische.paulkreft.backend.exception;

public class OAuth2LoginException extends RuntimeException {
    public OAuth2LoginException(String errorMessage) {
        super(errorMessage);
    }
}
