package de.neuefische.paulkreft.backend.exception;

public class LobbyGoneException extends RuntimeException {
    public LobbyGoneException(String errorMessage) {
        super(errorMessage);
    }
}
