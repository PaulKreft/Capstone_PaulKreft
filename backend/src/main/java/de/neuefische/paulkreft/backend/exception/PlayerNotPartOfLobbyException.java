package de.neuefische.paulkreft.backend.exception;

public class PlayerNotPartOfLobbyException extends RuntimeException {
    public PlayerNotPartOfLobbyException(String errorMessage) {
        super(errorMessage);
    }
}
