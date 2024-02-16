package de.neuefische.paulkreft.backend.lobby.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.neuefische.paulkreft.backend.exception.LobbyNotFoundException;
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


    public Lobby updateLobby(Lobby lobby) {
        if (!lobbyRepo.existsById(lobby.id())) {
            throw new LobbyNotFoundException("Could not find lobby");
        }

        return lobbyRepo.save(lobby);
    }

    public Lobby joinLobby(String id, @RequestBody Player player) {
        Lobby lobby = getLobbyById(id);

        if(lobby.players().contains(player)) {
            return lobby;
        }

        lobby.players().add(player);
        return lobbyRepo.save(lobby);
    }

    public Lobby leaveLobby(String id, @RequestBody Player player) {
        Lobby lobby = getLobbyById(id);

        lobby.players().remove(player);
        return lobbyRepo.save(lobby);
    }

    public Lobby deleteLobby(String id) {
        Lobby lobby = getLobbyById(id);

        lobbyRepo.deleteById(id);
        return lobby;
    }

    public Lobby setWinner(String id, ObjectNode payload) throws JsonProcessingException {
        Lobby lobby = getLobbyById(id);

        Player player = new ObjectMapper().treeToValue(payload.get("player"), Player.class);
        Integer time = payload.get("time").asInt();


        Player winner = lobby.winner();

        if (winner == null) {
            lobbyRepo.save(lobby.withWinner(player).withTimeToBeat(time));
        }

        if (lobby.timeToBeat() != null && lobby.timeToBeat() <= time) {
            lobby.losers().add(player);
            return lobbyRepo.save(lobby);
        }

        lobby.losers().add(lobby.winner());
        return lobbyRepo.save(lobby.withWinner(player).withTimeToBeat(time));
    }

    public Lobby setLoser(String id, Player loser) {
        Lobby lobby = getLobbyById(id);

        lobby.losers().add(loser);
        return lobbyRepo.save(lobby);
    }

}
