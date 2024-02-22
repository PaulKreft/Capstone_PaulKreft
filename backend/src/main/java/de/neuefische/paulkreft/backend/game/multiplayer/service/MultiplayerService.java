package de.neuefische.paulkreft.backend.game.multiplayer.service;

import de.neuefische.paulkreft.backend.game.multiplayer.model.MultiplayerGame;
import de.neuefische.paulkreft.backend.game.multiplayer.model.MultiplayerGameCreate;
import de.neuefische.paulkreft.backend.game.multiplayer.repository.MultiplayerGameRepo;
import de.neuefische.paulkreft.backend.utils.service.IdService;
import de.neuefische.paulkreft.backend.utils.service.TimeService;
import org.springframework.stereotype.Service;

@Service
public class MultiplayerService {
    private final MultiplayerGameRepo multiplayerGameRepo;
    private final IdService idService;
    private final TimeService timeService;

    public MultiplayerService(MultiplayerGameRepo multiplayerGameRepo, IdService idService, TimeService timeService) {
        this.multiplayerGameRepo = multiplayerGameRepo;
        this.idService = idService;
        this.timeService = timeService;
    }

    public MultiplayerGame createGame(MultiplayerGameCreate multiplayerGameCreate) {
        return multiplayerGameRepo.save(multiplayerGameCreate.withIdAndCreatedAt(idService.generateUUID(), timeService.getNow()));
    }
}
