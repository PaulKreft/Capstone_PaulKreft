package de.neuefische.paulkreft.backend.game.service;

import de.neuefische.paulkreft.backend.game.classic.model.ClassicGame;
import de.neuefische.paulkreft.backend.game.classic.model.GameCreate;
import de.neuefische.paulkreft.backend.game.classic.repository.GameRepo;
import de.neuefische.paulkreft.backend.game.classic.service.GameService;
import de.neuefische.paulkreft.backend.utils.service.IdService;
import de.neuefische.paulkreft.backend.utils.service.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClassicGameServiceTest {

    private final GameRepo gameRepo = mock(GameRepo.class);
    private final IdService idService = mock(IdService.class);
    private final TimeService timeService = mock(TimeService.class);

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService(gameRepo, idService, timeService);
    }

    @Test
    void createGame_shouldSaveGame() {
        // Given
        GameCreate mockGame = mock(GameCreate.class);

        Instant now = Instant.now();
        ClassicGame expectedGame = new ClassicGame("1", "User1", "Type", 1, true, 10, Collections.emptyList(), now);

        GameCreate gameCreate = new GameCreate("User1", "Type", 1, true, 10, Collections.emptyList());
        when(idService.generateUUID()).thenReturn("1");
        when(timeService.getNow()).thenReturn(now);
        when(mockGame.withIdAndCreatedAt("1", now)).thenReturn(expectedGame);

        when(gameRepo.save(any(ClassicGame.class))).thenReturn(expectedGame);

        // When
        ClassicGame savedGame = gameService.createGame(gameCreate);

        // Then
        assertEquals(expectedGame, savedGame);
        verify(gameRepo, times(1)).save(any(ClassicGame.class));
    }

    @Test
    void getGamesByUserId_shouldReturnGames() {
        // Given
        String userId = "User1";
        List<ClassicGame> expectedGames = Collections.singletonList(new ClassicGame("1", userId, "Type", 1, true, 10, Collections.emptyList(), Instant.now()));
        when(gameRepo.findAllByUserId(userId)).thenReturn(expectedGames);

        // When
        List<ClassicGame> actualGames = gameService.getGamesByUserId(userId);

        // Then
        assertEquals(expectedGames, actualGames);
        verify(gameRepo, times(1)).findAllByUserId(userId);
    }

}