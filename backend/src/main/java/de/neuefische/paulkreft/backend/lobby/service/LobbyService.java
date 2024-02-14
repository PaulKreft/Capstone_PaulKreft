package de.neuefische.paulkreft.backend.lobby.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    public Lobby updateLobby(Lobby lobby) {
        if (!lobbyRepo.existsById(lobby.id())) {
            throw new RuntimeException();
        }

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

    public Lobby setWinner(String id, ObjectNode payload) throws JsonProcessingException {
        Lobby lobby = lobbyRepo.findById(id).orElseThrow(RuntimeException::new);

        Player player = new ObjectMapper().treeToValue(payload.get("player"), Player.class);
        Integer time = payload.get("time").asInt();


        Player winner = lobby.winner();

        if (winner != null) {
            if (lobby.timeToBeat() <= time) {
                lobby.losers().add(player);
                return lobbyRepo.save(lobby);
            } else {
                lobby.losers().add(lobby.winner());
                return lobbyRepo.save(lobby.withWinner(player).withTimeToBeat(time));
            }
        }

        return lobbyRepo.save(lobby.withWinner(player).withTimeToBeat(time));
    }

    public Lobby setLoser(String id, Player loser) {
        Lobby lobby = lobbyRepo.findById(id).orElseThrow(RuntimeException::new);

        lobby.losers().add(loser);
        return lobbyRepo.save(lobby);
    }

}
