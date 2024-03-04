package de.neuefische.paulkreft.backend.lobby.model;

import de.neuefische.paulkreft.backend.user.model.Player;
import lombok.With;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

@With
public record Lobby(
        @Id
        String id,
        Player host,
        List<Player> players,
        boolean isGameInProgress,
        boolean isGameOver,
        int difficulty,
        Player winner,
        List<Player> losers,
        Integer streakToWin,
        Integer timeToBeat,
        Instant lastGameStarted,
        Integer capacity,
        Chat chat
) {
}
