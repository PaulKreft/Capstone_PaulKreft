package de.neuefische.paulkreft.backend.game.controller;

import de.neuefische.paulkreft.backend.game.classic.model.ClassicGame;
import de.neuefische.paulkreft.backend.game.classic.repository.GameRepo;
import de.neuefische.paulkreft.backend.utils.service.IdService;
import de.neuefische.paulkreft.backend.utils.service.TimeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClassicGameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdService idService;

    @MockBean
    private TimeService timeService;

    @Autowired
    private GameRepo gameRepo;


    @DirtiesContext
    @Test
    void createGameTest_whenGameCreateGiven_thenReturnGame() throws Exception {
        Instant now = Instant.parse("2016-06-09T00:00:00Z");
        when(timeService.getNow()).thenReturn(now);
        when(idService.generateUUID()).thenReturn("gameId");

        // Given, when and then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "userId": "User1",
                                    "type": "Type",
                                    "difficulty": 1,
                                    "isSuccess": true,
                                    "duration": 10,
                                    "configuration": []
                                 }
                                """)
                )

                // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                         {
                            "id": "gameId",
                            "userId": "User1",
                            "type": "Type",
                            "difficulty": 1,
                            "isSuccess": true,
                            "duration": 10,
                            "configuration": [],
                            "createdAt": "2016-06-09T00:00:00Z"
                         }
                        """));

        Mockito.verify(timeService, Mockito.times(1)).getNow();
        Mockito.verify(idService, Mockito.times(1)).generateUUID();
        verifyNoMoreInteractions(timeService, idService);
    }

    @DirtiesContext
    @Test
    void getGamesByUserIdTest_whenOneMatchingGameInDB_thenReturnMatchingGame() throws Exception {
        // Given
        Instant now = Instant.parse("2016-06-09T00:00:00Z");
        ClassicGame testGame = new ClassicGame("1", "User1", "Type", 1, true, 10, List.of("#FFFFFF"), now);
        ClassicGame testGame2 = new ClassicGame("2", "User2", "Type", 1, true, 10, Collections.emptyList(), now);
        gameRepo.save(testGame);
        gameRepo.save(testGame2);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/game/user/User1"))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                            "id": "1",
                            "userId": "User1",
                            "type": "Type",
                            "difficulty": 1,
                            "isSuccess": true,
                            "duration": 10,
                            "configuration": ["#FFFFFF"],
                            "createdAt": "2016-06-09T00:00:00Z"
                        }]
                        """));


        verifyNoInteractions(idService, timeService);
    }
}