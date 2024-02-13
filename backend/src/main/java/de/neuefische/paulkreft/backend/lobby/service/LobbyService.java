package de.neuefische.paulkreft.backend.lobby.service;

import de.neuefische.paulkreft.backend.lobby.model.Lobby;
import de.neuefische.paulkreft.backend.lobby.repository.LobbyRepo;
import de.neuefische.paulkreft.backend.users.models.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class LobbyService {
    private final LobbyRepo lobbyRepo;


    public Lobby createLobby(Lobby lobby) {
        return lobbyRepo.save(lobby);
    }

    public Lobby getLobbyById(String id) {
        return lobbyRepo.findById(id).orElseThrow(RuntimeException::new);
    }

    public Lobby joinLobby(String id, @RequestBody Player player) {
        Lobby lobby = lobbyRepo.findById(id).orElseThrow(RuntimeException::new);

        lobby.players().add(player);
        return lobbyRepo.save(lobby);
    }

    public Lobby leaveLobby(String id, @RequestBody Player player) {
        Lobby lobby = lobbyRepo.findById(id).orElseThrow(RuntimeException::new);

        lobby.players().remove(player);
        return lobbyRepo.save(lobby);
    }

    public Lobby deleteLobby(String id) {
        Lobby lobby = lobbyRepo.findById(id).orElseThrow(RuntimeException::new);

        lobbyRepo.deleteById(id);
        return lobby;
    }
}
