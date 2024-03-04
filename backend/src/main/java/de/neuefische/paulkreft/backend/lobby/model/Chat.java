package de.neuefische.paulkreft.backend.lobby.model;

import java.util.List;

public record Chat(
        List<ChatMessage> messages
) {
}
