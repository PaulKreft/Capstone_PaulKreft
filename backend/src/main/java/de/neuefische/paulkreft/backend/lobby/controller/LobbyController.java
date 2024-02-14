package de.neuefische.paulkreft.backend.lobby.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.neuefische.paulkreft.backend.lobby.model.Lobby;
import de.neuefische.paulkreft.backend.lobby.service.LobbyService;
import de.neuefische.paulkreft.backend.users.models.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lobby")
@RequiredArgsConstructor
public class LobbyController {
    private final LobbyService lobbyService;

    @PostMapping
    public Lobby createLobby(@RequestBody Lobby lobby) {
        return lobbyService.createLobby(lobby);
    }

    @GetMapping("/{id}")
    public Lobby getLobbyById(@PathVariable String id) {
        return lobbyService.getLobbyById(id);
    }

    @PutMapping("/{id}/join")
    public Lobby joinLobby(@PathVariable String id, @RequestBody Player player) {
        return lobbyService.joinLobby(id, player);
    }

    @PutMapping
    public Lobby updateLobby(@RequestBody Lobby lobby) {
        return lobbyService.updateLobby(lobby);
    }

    @PutMapping("/{id}/leave")
    public Lobby leaveLobby(@PathVariable String id, @RequestBody Player player) {
        return lobbyService.leaveLobby(id, player);
    }

    @DeleteMapping("/{id}")
    public Lobby deleteLobby(@PathVariable String id) {
        return lobbyService.deleteLobby(id);
    }

    @PutMapping("/{id}/setWinner")
    public Lobby setWinner(@PathVariable String id, @RequestBody ObjectNode payload) throws JsonProcessingException {
        return lobbyService.setWinner(id, payload);
    }

    @PutMapping("/{id}/setLoser")
    public Lobby setLoser(@PathVariable String id, @RequestBody Player player) {
        return lobbyService.setLoser(id, player);
    }
}
