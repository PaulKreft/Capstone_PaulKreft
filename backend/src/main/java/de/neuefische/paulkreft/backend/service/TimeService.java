package de.neuefische.paulkreft.backend.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TimeService {

    public Instant getNow() {
        return Instant.now();
    }
}
