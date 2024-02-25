package de.neuefische.paulkreft.backend.game.multiplayer.repository;

import de.neuefische.paulkreft.backend.game.multiplayer.model.MultiplayerGame;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultiplayerGameRepo extends MongoRepository<MultiplayerGame, String> {
    List<MultiplayerGame> findAllByTotalPlayersAndPlayerIdsIsContainingOrderByCreatedAtAsc(int totalPlayers, List<String> playerIds);
}