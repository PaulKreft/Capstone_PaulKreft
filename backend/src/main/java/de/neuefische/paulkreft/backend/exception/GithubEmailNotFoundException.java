package de.neuefische.paulkreft.backend.exception;

public class GithubEmailNotFoundException extends RuntimeException {
    public GithubEmailNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
