package de.neuefische.paulkreft.backend.exception;

public class LobbyCapacityExceededException extends RuntimeException {
    public LobbyCapacityExceededException(String errorMessage) {
        super(errorMessage);
    }
}
