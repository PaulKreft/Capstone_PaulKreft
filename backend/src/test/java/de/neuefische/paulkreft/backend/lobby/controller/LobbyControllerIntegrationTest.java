package de.neuefische.paulkreft.backend.lobby.controller;

import de.neuefische.paulkreft.backend.lobby.model.Lobby;
import de.neuefische.paulkreft.backend.lobby.repository.LobbyRepo;
import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.services.TimeService;
import de.neuefische.paulkreft.backend.users.models.Player;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class LobbyControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LobbyRepo lobbyRepo;

    private Lobby testLobby;

    @BeforeEach
    public void instantiateTestLobby() {
        Player host = new Player("1", "Paul");
        Player participant = new Player("2", "Soso");
        testLobby = new Lobby("1", host, List.of(host, participant), false, false, 4, null, List.of(), 3, null, null);
    }


    @Test
    void createLobbyTest_whenCreateLobby_thenReturnGame() throws Exception {

        // Given, when and then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/lobby")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "id": "1",
                                    "host": { "id" :  "1", "name":  "Paul"},
                                    "players": [{ "id" :  "1", "name":  "Paul"}, { "id" :  "2", "name":  "Soso"}],
                                    "isGameInProgress": false,
                                    "isGameOver": false,
                                    "difficulty": 4,
                                    "losers": [],
                                    "streakToWin": 3
                                 }
                                """)
                )

                // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                 {
                                    "id": "1",
                                    "host": { "id" :  "1", "name":  "Paul"},
                                    "players": [{ "id" :  "1", "name":  "Paul"}, { "id" :  "2", "name":  "Soso"}],
                                    "isGameInProgress": false,
                                    "isGameOver": false,
                                    "difficulty": 4,
                                    "winner": null,
                                    "losers": [],
                                    "streakToWin": 3,
                                    "timeToBeat": null,
                                    "lastGameStarted": null
                                 }
                        """));
    }
}