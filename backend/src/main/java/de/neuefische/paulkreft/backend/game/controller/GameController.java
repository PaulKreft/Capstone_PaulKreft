package de.neuefische.paulkreft.backend.game.controller;

import de.neuefische.paulkreft.backend.game.model.Game;
import de.neuefische.paulkreft.backend.game.model.GameCreate;
import de.neuefische.paulkreft.backend.game.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public Game createGame(@RequestBody GameCreate gameCreate) {
        return gameService.createGame(gameCreate);
    }

    @GetMapping("/user/{userId}")
    public List<Game> getGamesByUserId(@PathVariable String userId) {
        return gameService.getGamesByUserId(userId);
    }
}
