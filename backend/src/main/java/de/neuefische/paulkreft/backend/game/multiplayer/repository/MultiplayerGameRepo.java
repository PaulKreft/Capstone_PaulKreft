package de.neuefische.paulkreft.backend.game.multiplayer.repository;

import de.neuefische.paulkreft.backend.game.multiplayer.model.MultiplayerGame;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiplayerGameRepo extends MongoRepository<MultiplayerGame, String> {
}