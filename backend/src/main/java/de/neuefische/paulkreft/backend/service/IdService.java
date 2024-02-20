package de.neuefische.paulkreft.backend.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService {
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
