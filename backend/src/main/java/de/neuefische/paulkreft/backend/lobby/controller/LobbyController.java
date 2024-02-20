package de.neuefische.paulkreft.backend.lobby.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.neuefische.paulkreft.backend.exception.LobbyNotFoundException;
import de.neuefische.paulkreft.backend.lobby.model.Lobby;
import de.neuefische.paulkreft.backend.lobby.service.LobbyService;
import de.neuefische.paulkreft.backend.user.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
@RequestMapping("/api/lobby")
@RequiredArgsConstructor
public class LobbyController {
    private final LobbyService lobbyService;

    private final Map<String, ConcurrentLinkedQueue<DeferredResult<Lobby>>> queuedRequests = new HashMap<>();

    @GetMapping("/{id}/long")
    public DeferredResult<Lobby> getGameUpdate(@PathVariable String id) {
        ConcurrentLinkedQueue<DeferredResult<Lobby>> queue = this.queuedRequests.get(id);

        if (queue == null) {
            throw new LobbyNotFoundException("Lobby does not exist in database");
        }

        DeferredResult<Lobby> updatedLobby = new DeferredResult<>();
        updatedLobby.onTimeout(() -> queue.remove(updatedLobby));
        queue.add(updatedLobby);
        return updatedLobby;
    }

    @PostMapping
    public Lobby createLobby(@RequestBody Lobby lobby) {
        queuedRequests.put(lobby.id(), new ConcurrentLinkedQueue<>());

        return lobbyService.createLobby(lobby);
    }

    @GetMapping("/{id}")
    public Lobby getLobbyById(@PathVariable String id) {
        return lobbyService.getLobbyById(id);
    }

    @PutMapping
    public Lobby updateLobby(@RequestBody Lobby lobby) {
        Lobby updatedLobby = lobbyService.updateLobby(lobby);

        resolveRequest(updatedLobby);
        return updatedLobby;
    }

    @PutMapping("/{id}/join")
    public Lobby joinLobby(@PathVariable String id, @RequestBody Player player) {
        Lobby updatedLobby = lobbyService.joinLobby(id, player);

        resolveRequest(updatedLobby);
        return updatedLobby;
    }

    @PutMapping("/{id}/leave")
    public Lobby leaveLobby(@PathVariable String id, @RequestBody Player player) {
        Lobby updatedLobby = lobbyService.leaveLobby(id, player);

        resolveRequest(updatedLobby);
        return updatedLobby;
    }

    @DeleteMapping("/{id}")
    public Lobby deleteLobby(@PathVariable String id) {
        return lobbyService.deleteLobby(id);
    }

    @PutMapping("/{id}/setWinner")
    public Lobby setWinner(@PathVariable String id, @RequestBody ObjectNode payload) throws JsonProcessingException {
        Lobby updatedLobby = lobbyService.setWinner(id, payload);

        resolveRequest(updatedLobby);
        return updatedLobby;
    }

    @PutMapping("/{id}/setLoser")
    public Lobby setLoser(@PathVariable String id, @RequestBody Player player) {
        Lobby updatedLobby = lobbyService.setLoser(id, player);

        resolveRequest(updatedLobby);
        return updatedLobby;
    }

    public void resolveRequest(Lobby lobby) {
        ConcurrentLinkedQueue<DeferredResult<Lobby>> queue = this.queuedRequests.get(lobby.id());

        if (queue == null) {
            throw new LobbyNotFoundException("Lobby does not exist in database");
        }

        for (DeferredResult<Lobby> defResult : queue) {
            defResult.setResult(lobby);
        }
    }
}
