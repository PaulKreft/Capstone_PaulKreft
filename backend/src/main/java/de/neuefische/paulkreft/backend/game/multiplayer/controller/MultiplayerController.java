package de.neuefische.paulkreft.backend.game.multiplayer.controller;

import de.neuefische.paulkreft.backend.game.multiplayer.model.MultiplayerGame;
import de.neuefische.paulkreft.backend.game.multiplayer.model.MultiplayerGameCreate;
import de.neuefische.paulkreft.backend.game.multiplayer.service.MultiplayerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game/multiplayer")
public class MultiplayerController {
    private final MultiplayerService multiplayerService;

    public MultiplayerController(MultiplayerService multiplayerService) {
        this.multiplayerService = multiplayerService;
    }

    @PostMapping
    public MultiplayerGame createMultiplayerGame(@RequestBody MultiplayerGameCreate multiplayerGameCreate) {
        return multiplayerService.createGame(multiplayerGameCreate);
    }
}
