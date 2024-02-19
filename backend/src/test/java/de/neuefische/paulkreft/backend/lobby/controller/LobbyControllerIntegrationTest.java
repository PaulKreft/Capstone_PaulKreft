package de.neuefische.paulkreft.backend.lobby.controller;

import de.neuefische.paulkreft.backend.exception.LobbyNotFoundException;
import de.neuefische.paulkreft.backend.exception.PlayerNotPartOfLobbyException;
import de.neuefische.paulkreft.backend.lobby.model.Lobby;
import de.neuefische.paulkreft.backend.lobby.repository.LobbyRepo;
import de.neuefische.paulkreft.backend.user.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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


    @DirtiesContext
    @Test
    void createLobbyTest_whenCreateLobby_thenReturnLobby() throws Exception {
        // Given and when
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

    @DirtiesContext
    @Test
    void getLobbyByIdTest_returnLobby() throws Exception {
        // Given
        lobbyRepo.save(testLobby);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/lobby/1"))
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

    @DirtiesContext
    @Test
    void getLobbyByIdTest_whenLobbyDoesNotExist_throwLobbyNotFoundException() throws Exception {
        // Given and when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/lobby/1"))
                // Then
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(LobbyNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string(""));
    }

    @DirtiesContext
    @Test
    void updateLobbyTest_whenUpdateLobby_thenReturnUpdatedLobby() throws Exception {
        // Given
        lobbyRepo.save(testLobby);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "id": "1",
                                    "host": { "id" :  "1", "name":  "Paul"},
                                    "players": [{ "id" :  "1", "name":  "Paul"}, { "id" :  "2", "name":  "Soso"}],
                                    "isGameInProgress": false,
                                    "isGameOver": false,
                                    "difficulty": 2,
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
                                    "difficulty": 2,
                                    "winner": null,
                                    "losers": [],
                                    "streakToWin": 3,
                                    "timeToBeat": null,
                                    "lastGameStarted": null
                                 }
                        """));
    }

    @DirtiesContext
    @Test
    void updateLobbyTest_whenUpdateNonExistingLobby_throwLobbyNotFoundException() throws Exception {
        // Given and when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "id": "1",
                                    "host": { "id" :  "1", "name":  "Paul"},
                                    "players": [{ "id" :  "1", "name":  "Paul"}, { "id" :  "2", "name":  "Soso"}],
                                    "isGameInProgress": false,
                                    "isGameOver": false,
                                    "difficulty": 2,
                                    "losers": [],
                                    "streakToWin": 3
                                 }
                                """)
                )

                // Then
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(LobbyNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string(""));
    }

    @DirtiesContext
    @Test
    void joinLobbyTest_whenJoiningLobbyThatDoesNotExist_throwLobbyNotFoundException() throws Exception {
        // Given & When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "id": "3",
                                    "name": "Jürgen"
                                 }
                                """)
                )

                // Then
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(LobbyNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string(""));
    }

    @DirtiesContext
    @Test
    void joinLobbyTest_whenJoiningLobby_returnUpdatedLobbyWithNewPlayer() throws Exception {
        // Given
        lobbyRepo.save(testLobby);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "id": "3",
                                    "name": "Jürgen"
                                 }
                                """)
                )

                // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                 {
                                    "id": "1",
                                    "host": { "id" :  "1", "name":  "Paul"},
                                    "players": [{ "id" :  "1", "name":  "Paul"}, { "id" :  "2", "name":  "Soso"}, { "id" :  "3", "name":  "Jürgen"}],
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

    @DirtiesContext
    @Test
    void joinLobbyTest_whenJoiningLobbyAgain_returnLobbyWithNewPlayerPresentOnlyOnce() throws Exception {
        // Given
        Player newPlayer = new Player("3", "Jürgen");
        List<Player> listIncludingNewPlayer = Stream.concat(testLobby.players().stream(), Stream.of(newPlayer)).toList();
        lobbyRepo.save(testLobby.withPlayers(listIncludingNewPlayer));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/lobby/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                 {
                                    "id": "1",
                                    "host": { "id" :  "1", "name":  "Paul"},
                                    "players": [{ "id" :  "1", "name":  "Paul"}, { "id" :  "2", "name":  "Soso"}, { "id" :  "3", "name":  "Jürgen"}],
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

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "id": "3",
                                    "name": "Jürgen"
                                 }
                                """)
                )

                // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                 {
                                    "id": "1",
                                    "host": { "id" :  "1", "name":  "Paul"},
                                    "players": [{ "id" :  "1", "name":  "Paul"}, { "id" :  "2", "name":  "Soso"}, { "id" :  "3", "name":  "Jürgen"}],
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

    @DirtiesContext
    @Test
    void leaveLobbyTest_whenLeavingLobbyThatDoesNotExist_throwLobbyNotFoundException() throws Exception {
        // Given & When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "id": "3",
                                    "name": "Jürgen"
                                 }
                                """)
                )

                // Then
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(LobbyNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string(""));
    }

    @DirtiesContext
    @Test
    void leaveLobbyTest_whenLeavingLobby_returnUpdatedLobbyWithoutPlayer() throws Exception {
        // Given
        lobbyRepo.save(testLobby);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/lobby/1"))
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

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "id": "2",
                                    "name": "Soso"
                                 }
                                """)
                )

                // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                 {
                                    "id": "1",
                                    "host": { "id" :  "1", "name":  "Paul"},
                                    "players": [{ "id" :  "1", "name":  "Paul"}],
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

    @DirtiesContext
    @Test
    void deleteLobbyTest_whenDeletingLobby_returnDeletedLobby() throws Exception {
        // Given
        lobbyRepo.save(testLobby);

        // When
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/lobby/1"))

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

        // & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/lobby/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(LobbyNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string(""));

    }

    @DirtiesContext
    @Test
    void deleteLobbyTest_whenDeletingLobbyThatDoesNotExist_throwLobbyNotFoundException() throws Exception {
        // Given & When
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/lobby/1"))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(LobbyNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string(""));
    }

    @DirtiesContext
    @Test
    void setWinnerTest_whenSettingWinnerOfLobbyThatDoesNotExist_throwLobbyNotFoundException() throws Exception {
        // Given & When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setWinner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "player":
                                    {
                                        "id": "2",
                                        "name": "Soso"
                                    },
                                 "time": 123
                                 }
                                """)
                )

                // Then
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(LobbyNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string(""));
    }

    @DirtiesContext
    @Test
    void setWinnerTest_whenSettingWinnerOfLobbyThatIsNotPartOfThatLobby_throwPlayerNotPartOfLobbyException() throws Exception {
        // Given
        lobbyRepo.save(testLobby);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setWinner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "player":
                                     {
                                        "id": "3",
                                        "name": "Jürgen"
                                     },
                                 "time": 123
                                 }
                                """)
                )

                // Then
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(PlayerNotPartOfLobbyException.class, result.getResolvedException()))
                .andExpect(content().string(""));
    }

    @DirtiesContext
    @Test
    void setWinnerTest_whenSettingWinnerOfLobby_returnLobbyWithWinnerAndTimeToBeat() throws Exception {
        // Given
        lobbyRepo.save(testLobby);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setWinner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "player":
                                     {
                                        "id": "2",
                                        "name": "Soso"
                                     },
                                 "time": 123
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
                                    "winner": { "id" :  "2", "name":  "Soso"},
                                    "losers": [],
                                    "streakToWin": 3,
                                    "timeToBeat": 123,
                                    "lastGameStarted": null
                                 }
                        """));
    }

    @DirtiesContext
    @Test
    void setWinnerTest_whenSettingWinnerWithBetterTimeThanWinnerAlreadySet_returnLobbyWithWinnerAndTimeToBeatAndOldWinnerPartOfLosers() throws Exception {
        // Given
        lobbyRepo.save(testLobby.withWinner(testLobby.host()).withTimeToBeat(500));

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setWinner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "player":
                                     {
                                        "id": "2",
                                        "name": "Soso"
                                     },
                                 "time": 123
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
                                    "winner": { "id" :  "2", "name":  "Soso"},
                                    "losers": [{ "id" :  "1", "name":  "Paul"}],
                                    "streakToWin": 3,
                                    "timeToBeat": 123,
                                    "lastGameStarted": null
                                 }
                        """));
    }

    @DirtiesContext
    @Test
    void setWinnerTest_whenSettingWinnerWithSameTimeThanWinnerAlreadySet_returnLobbyWithWinnerAndTimeToBeatAndOldWinnerPartOfLosers() throws Exception {
        // Given
        lobbyRepo.save(testLobby.withWinner(testLobby.host()).withTimeToBeat(123));

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setWinner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "player":
                                     {
                                        "id": "2",
                                        "name": "Soso"
                                     },
                                 "time": 123
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
                                    "winner": { "id" :  "2", "name":  "Soso"},
                                    "losers": [{ "id" :  "1", "name":  "Paul"}],
                                    "streakToWin": 3,
                                    "timeToBeat": 123,
                                    "lastGameStarted": null
                                 }
                        """));
    }

    @DirtiesContext
    @Test
    void setWinnerTest_whenSettingWinnerWithWorseTimeThanWinnerAlreadySet_returnLobbyWithNewWinnerAsPartOfLosers() throws Exception {
        // Given
        lobbyRepo.save(testLobby.withWinner(testLobby.host()).withTimeToBeat(123));

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setWinner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "player":
                                     {
                                        "id": "2",
                                        "name": "Soso"
                                     },
                                 "time": 500
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
                                    "winner": { "id" :  "1", "name":  "Paul"},
                                    "losers": [{ "id" :  "2", "name":  "Soso"}],
                                    "streakToWin": 3,
                                    "timeToBeat": 123,
                                    "lastGameStarted": null
                                 }
                        """));
    }

    @DirtiesContext
    @Test
    void setLoserTest_whenSettingLoserOfLobbyThatDoesNotExist_throwLobbyNotFoundException() throws Exception {
        // Given & When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setLoser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "id": "2",
                                        "name": "Soso"
                                    }
                                """)
                )

                // Then
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(LobbyNotFoundException.class, result.getResolvedException()))
                .andExpect(content().string(""));
    }


    @DirtiesContext
    @Test
    void setLoserTest_whenSettingLoserOfLobbyThatIsNotPartOfThatLobby_throwPlayerNotPartOfLobbyException() throws Exception {
        // Given
        lobbyRepo.save(testLobby);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setLoser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                     {
                                        "id": "3",
                                        "name": "Jürgen"
                                     }
                                """)
                )

                // Then
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(PlayerNotPartOfLobbyException.class, result.getResolvedException()))
                .andExpect(content().string(""));
    }

    @DirtiesContext
    @Test
    void setLoserTest_whenSettingLoserOfLobby_returnLobbyWithPlayerAsPartOfLosers() throws Exception {
        // Given
        lobbyRepo.save(testLobby);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setLoser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "id": "2",
                                        "name": "Soso"
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
                                    "losers": [{ "id" :  "2", "name":  "Soso"}],
                                    "streakToWin": 3,
                                    "timeToBeat": null,
                                    "lastGameStarted": null
                                 }
                        """));
    }

    @DirtiesContext
    @Test
    void setWinnerTest_whenSettingWinnerWithoutTime_returnIllegalArgumentException() throws Exception {
        // Given
        lobbyRepo.save(testLobby);

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setWinner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "player":
                                     {
                                        "id": "2",
                                        "name": "Soso"
                                     }
                                 }
                                """)
                )

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(IllegalArgumentException.class, result.getResolvedException()))
                .andExpect(content().string(""));
    }

    @DirtiesContext
    @Test
    void setWinnerTest_whenSettingWinnerWhenWinnerWithNoTimeAlreadySet_returnLobbyWithWinnerAndTimeToBeatAndOldWinnerPartOfLosers() throws Exception {
        // Given
        lobbyRepo.save(testLobby.withWinner(testLobby.host()));

        // When
        mockMvc.perform(MockMvcRequestBuilders.put("/api/lobby/1/setWinner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 { "player":
                                     {
                                        "id": "2",
                                        "name": "Soso"
                                     },
                                 "time": 5000
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
                                    "winner": { "id" :  "2", "name":  "Soso"},
                                    "losers": [{ "id" :  "1", "name":  "Paul"}],
                                    "streakToWin": 3,
                                    "timeToBeat": 5000,
                                    "lastGameStarted": null
                                 }
                        """));
    }
}
