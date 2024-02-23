package de.neuefische.paulkreft.backend.game.classic.service;

import de.neuefische.paulkreft.backend.game.classic.model.ClassicGame;
import de.neuefische.paulkreft.backend.game.classic.model.GameCreate;
import de.neuefische.paulkreft.backend.game.classic.repository.GameRepo;
import de.neuefische.paulkreft.backend.utils.service.IdService;
import de.neuefische.paulkreft.backend.utils.service.TimeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepo gameRepo;
    private final IdService idService;
    private final TimeService timeService;

    public GameService(GameRepo gameRepo, IdService idService, TimeService timeService) {
        this.gameRepo = gameRepo;
        this.idService = idService;
        this.timeService = timeService;
    }

    public ClassicGame createGame(GameCreate gameCreate) {
        return gameRepo.save(gameCreate.withIdAndCreatedAt(idService.generateUUID(), timeService.getNow()));
    }

    public List<ClassicGame> getGamesByUserId(String userId) {
        return gameRepo.findAllByUserId(userId);
    }
}
