package de.neuefische.paulkreft.backend.game.classic.repository;

import de.neuefische.paulkreft.backend.game.classic.model.ClassicGame;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepo extends MongoRepository<ClassicGame, String> {
    List<ClassicGame> findAllByUserId(String id);
    List<ClassicGame> findAllByUserIdOrderByCreatedAtAsc(String id);
}