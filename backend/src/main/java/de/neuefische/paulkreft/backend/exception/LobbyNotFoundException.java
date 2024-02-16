package de.neuefische.paulkreft.backend.exception;

public class LobbyNotFoundException extends RuntimeException {
    public LobbyNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
