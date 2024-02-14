package de.neuefische.paulkreft.backend.lobby.repository;

import de.neuefische.paulkreft.backend.lobby.model.Lobby;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LobbyRepo extends MongoRepository<Lobby, String> {
}
