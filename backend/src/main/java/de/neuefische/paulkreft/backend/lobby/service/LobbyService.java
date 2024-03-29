package de.neuefische.paulkreft.backend.lobby.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.neuefische.paulkreft.backend.exception.LobbyCapacityExceededException;
import de.neuefische.paulkreft.backend.exception.LobbyGoneException;
import de.neuefische.paulkreft.backend.exception.PlayerNotPartOfLobbyException;
import de.neuefische.paulkreft.backend.lobby.model.Lobby;
import de.neuefische.paulkreft.backend.lobby.repository.LobbyRepo;
import de.neuefische.paulkreft.backend.user.model.Player;
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
        return lobbyRepo.findById(id).orElseThrow(() -> new LobbyGoneException("Could not find lobby"));
    }


    public Lobby updateLobby(Lobby lobby) {
        if (!lobbyRepo.existsById(lobby.id())) {
            throw new LobbyGoneException("Could not find lobby");
        }

        return lobbyRepo.save(lobby);
    }

    public Lobby joinLobby(String id, @RequestBody Player player) {
        Lobby lobby = getLobbyById(id);

        if (lobby.players().contains(player)) {
            return lobby;
        }

        if (lobby.capacity() != null && lobby.players().size() == lobby.capacity()) {
            throw new LobbyCapacityExceededException("This lobby is full");
        }

        lobby.players().add(player);
        return lobbyRepo.save(lobby);
    }

    public Lobby leaveLobby(String id, @RequestBody Player player) {
        Lobby lobby = getLobbyById(id);

        lobby.players().remove(player);

        if (lobby.players().isEmpty()) {
            lobbyRepo.deleteById(id);
            return lobby;
        }

        if (lobby.host().equals(player)) {
            return lobbyRepo.save(lobby.withHost(lobby.players().getFirst()));
        }

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
        JsonNode timeNode = payload.get("time");

        if (timeNode == null) {
            throw new IllegalArgumentException("Time cannot be empty when setting winner");
        }

        int time = timeNode.asInt();

        Player winner = lobby.winner();

        if (!lobby.players().contains(player)) {
            throw new PlayerNotPartOfLobbyException("Trying to set winner that is not in the lobby");
        }

        if (winner == null) {
            return lobbyRepo.save(lobby.withWinner(player).withTimeToBeat(time));
        }

        if (lobby.timeToBeat() != null && lobby.timeToBeat() < time) {
            lobby.losers().add(player);
            return lobbyRepo.save(lobby);
        }

        lobby.losers().add(lobby.winner());
        return lobbyRepo.save(lobby.withWinner(player).withTimeToBeat(time));
    }

    public Lobby setLoser(String id, Player loser) {
        Lobby lobby = getLobbyById(id);


        if (!lobby.players().contains(loser)) {
            throw new PlayerNotPartOfLobbyException("Trying to set loser that is not in the lobby");
        }

        lobby.losers().add(loser);
        return lobbyRepo.save(lobby);
    }

}
