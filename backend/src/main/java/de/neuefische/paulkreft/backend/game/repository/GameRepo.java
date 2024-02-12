package de.neuefische.paulkreft.backend.game.repository;

import de.neuefische.paulkreft.backend.game.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepo extends MongoRepository<Game, String> {
    List<Game> findAllByUserId(String id);
    List<Game> findAllByUserIdOrderByCreatedAtAsc(String id);
}