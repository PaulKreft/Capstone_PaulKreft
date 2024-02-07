package de.neuefische.paulkreft.backend.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String errorMessage) {
        super(errorMessage);
    }
}
