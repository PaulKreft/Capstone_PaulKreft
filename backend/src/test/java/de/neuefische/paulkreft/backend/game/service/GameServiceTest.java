package de.neuefische.paulkreft.backend.game.service;

import de.neuefische.paulkreft.backend.game.model.Game;
import de.neuefische.paulkreft.backend.game.model.GameCreate;
import de.neuefische.paulkreft.backend.game.repository.GameRepo;
import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private GameRepo gameRepo;
    private GameService gameService;
    private IdService idService;
    private TimeService timeService;

    @BeforeEach
    void setUp() {
        gameRepo = mock(GameRepo.class);
        idService = mock(IdService.class);
        timeService = mock(TimeService.class);
        gameService = new GameService(gameRepo, idService, timeService);
    }

    @Test
    void createGame_shouldSaveGame() {
        // Given
        GameCreate mockGame = mock(GameCreate.class);

        Instant now = Instant.now();
        Game expectedGame = new Game("1", "User1", "Type", 1, true, 10, Collections.emptyList(), now);

        GameCreate gameCreate = new GameCreate("User1", "Type", 1, true, 10, Collections.emptyList());
        when(idService.generateUUID()).thenReturn("1");
        when(timeService.getNow()).thenReturn(now);
        when(mockGame.withIdAndCreatedAt("1", now)).thenReturn(expectedGame);

        when(gameRepo.save(any(Game.class))).thenReturn(expectedGame);

        // When
        Game savedGame = gameService.createGame(gameCreate);

        // Then
        assertEquals(expectedGame, savedGame);
        verify(gameRepo, times(1)).save(any(Game.class));
    }

    @Test
    void getGamesByUserId_shouldReturnGames() {
        // Given
        String userId = "User1";
        List<Game> expectedGames = Collections.singletonList(new Game("1", userId, "Type", 1, true, 10, Collections.emptyList(), Instant.now()));
        when(gameRepo.findAllByUserId(userId)).thenReturn(expectedGames);

        // When
        List<Game> actualGames = gameService.getGamesByUserId(userId);

        // Then
        assertEquals(expectedGames, actualGames);
        verify(gameRepo, times(1)).findAllByUserId(userId);
    }

}