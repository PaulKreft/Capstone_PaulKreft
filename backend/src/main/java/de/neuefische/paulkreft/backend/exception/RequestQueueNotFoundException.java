package de.neuefische.paulkreft.backend.exception;

public class RequestQueueNotFoundException extends RuntimeException {
    public RequestQueueNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
