package de.neuefische.paulkreft.backend.lobby.controller;

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

    @PutMapping("/{id}/leave")
    public Lobby leaveLobby(@PathVariable String id, @RequestBody Player player) {
        return lobbyService.leaveLobby(id, player);
    }

    @DeleteMapping("/{id}")
    public Lobby deleteLobby(@PathVariable String id) {
        return lobbyService.deleteLobby(id);
    }
}
