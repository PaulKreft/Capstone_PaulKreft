package de.neuefische.paulkreft.backend.lobby.model;

import de.neuefische.paulkreft.backend.users.models.Player;
import org.springframework.data.annotation.Id;

import java.util.List;

public record Lobby(
        @Id
        String id,
        List<Player> players,
        boolean isGameInProgress,
        boolean isGameOver,
        int difficulty,
        Player winner
) {
}
