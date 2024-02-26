package de.neuefische.paulkreft.backend.statistic.service;

import de.neuefische.paulkreft.backend.game.classic.model.Game;
import de.neuefische.paulkreft.backend.game.classic.repository.GameRepo;
import de.neuefische.paulkreft.backend.game.multiplayer.model.MultiplayerGame;
import de.neuefische.paulkreft.backend.game.multiplayer.repository.MultiplayerGameRepo;
import de.neuefische.paulkreft.backend.statistic.model.ClassicStatistics;
import de.neuefische.paulkreft.backend.statistic.model.DuelStatistics;
import de.neuefische.paulkreft.backend.statistic.model.ScoreMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class StatisticService {
    private final GameRepo gameRepo;
    private final MultiplayerGameRepo multiplayerGameRepo;

    private static final int EASY = 1;
    private static final int MEDIUM = 2;
    private static final int HARD = 4;

    private static final int DUEL_TOTAL_PLAYERS = 2;

    public ClassicStatistics getUserClassicStatistics(String id) {
        List<Game> games = gameRepo.findAllByUserIdOrderByCreatedAtAsc(id);

        List<Game> easyGames = games.stream().filter(game -> game.difficulty() == EASY).toList();
        List<Game> mediumGames = games.stream().filter(game -> game.difficulty() == MEDIUM).toList();
        List<Game> hardGames = games.stream().filter(game -> game.difficulty() == HARD).toList();

        ScoreMap longestWinningStreaks = getLongestWinningStreaks(easyGames, mediumGames, hardGames);
        ScoreMap longestLosingStreaks = getLongestLosingStreaks(easyGames, mediumGames, hardGames);
        ScoreMap totalGames = getTotalGames(easyGames, mediumGames, hardGames);
        ScoreMap totalGamesWon = getGamesWon(easyGames, mediumGames, hardGames);
        ScoreMap fastestSolve = getFastestSolves(easyGames, mediumGames, hardGames);
        ScoreMap averageDuration = getAverageDurations(easyGames, mediumGames, hardGames);

        return new ClassicStatistics(longestWinningStreaks, longestLosingStreaks, totalGames, totalGamesWon, fastestSolve, averageDuration);
    }


    public DuelStatistics getUserDuelStatistics(String id, String opponentId) {
        List<MultiplayerGame> games = multiplayerGameRepo.findAllByTotalPlayersAndPlayerIdsIsContainingOrderByCreatedAtAsc(DUEL_TOTAL_PLAYERS, List.of(id, opponentId));

        List<MultiplayerGame> easyGames = games.stream().filter(game -> game.difficulty() == EASY).toList();
        List<MultiplayerGame> mediumGames = games.stream().filter(game -> game.difficulty() == MEDIUM).toList();
        List<MultiplayerGame> hardGames = games.stream().filter(game -> game.difficulty() == HARD).toList();

        double easyGamesWon = easyGames.stream().filter(game -> game.winnerIds().contains(id)).toList().size();
        double mediumGamesWon = mediumGames.stream().filter(game -> game.winnerIds().contains(id)).toList().size();
        double hardGamesWon = hardGames.stream().filter(game -> game.winnerIds().contains(id)).toList().size();

        return new DuelStatistics(id, opponentId, new ScoreMap((double) easyGames.size(), (double) mediumGames.size(), (double) hardGames.size()), new ScoreMap(easyGamesWon, mediumGamesWon, hardGamesWon));
    }


    private ScoreMap getAverageDurations(List<Game> easyGames, List<Game> mediumGames, List<Game> hardGames) {
        List<Double> easyDurations = easyGames.stream().map(game -> Double.valueOf(game.duration())).filter(d -> d != 0).toList();
        List<Double> mediumDurations = mediumGames.stream().map(game -> Double.valueOf(game.duration())).filter(d -> d != 0).toList();
        List<Double> hardDurations = hardGames.stream().map(game -> Double.valueOf(game.duration())).filter(d -> d != 0).toList();

        Double averageDurationEasy = !easyDurations.isEmpty() ? easyDurations.stream().mapToDouble(v -> v).sum() / easyDurations.size() : null;
        Double averageDurationMedium = !mediumDurations.isEmpty() ? mediumDurations.stream().mapToDouble(v -> v).sum() / mediumDurations.size() : null;
        Double averageDurationHard = !hardDurations.isEmpty() ? hardDurations.stream().mapToDouble(v -> v).sum() / hardDurations.size() : null;

        return new ScoreMap(averageDurationEasy, averageDurationMedium, averageDurationHard);
    }

    private ScoreMap getFastestSolves(List<Game> easyGames, List<Game> mediumGames, List<Game> hardGames) {
        List<Double> easyDurations = easyGames.stream().map(game -> Double.valueOf(game.duration())).filter(d -> d != 0).toList();
        List<Double> mediumDurations = mediumGames.stream().map(game -> Double.valueOf(game.duration())).filter(d -> d != 0).toList();
        List<Double> hardDurations = hardGames.stream().map(game -> Double.valueOf(game.duration())).filter(d -> d != 0).toList();

        Double fastestSolveEasy = !easyDurations.isEmpty() ? Collections.min(easyDurations) : null;
        Double fastestSolveMedium = !mediumDurations.isEmpty() ? Collections.min(mediumDurations) : null;
        Double fastestSolveHard = !hardDurations.isEmpty() ? Collections.min(hardDurations) : null;

        return new ScoreMap(fastestSolveEasy, fastestSolveMedium, fastestSolveHard);
    }

    private ScoreMap getTotalGames(List<Game> easyGames, List<Game> mediumGames, List<Game> hardGames) {
        return new ScoreMap((double) easyGames.size(), (double) mediumGames.size(), (double) hardGames.size());
    }

    private ScoreMap getGamesWon(List<Game> easyGames, List<Game> mediumGames, List<Game> hardGames) {
        return new ScoreMap((double) easyGames.stream().filter(Game::isSuccess).toList().size(), (double) mediumGames.stream().filter(Game::isSuccess).toList().size(), (double) hardGames.stream().filter(Game::isSuccess).toList().size());
    }

    private ScoreMap getLongestWinningStreaks(List<Game> easyGames, List<Game> mediumGames, List<Game> hardGames) {
        Map<String, Double> easyStreaks = getStreaks(easyGames);
        Map<String, Double> mediumStreaks = getStreaks(mediumGames);
        Map<String, Double> hardStreaks = getStreaks(hardGames);


        return new ScoreMap(easyStreaks.get("win"), mediumStreaks.get("win"), hardStreaks.get("win"));
    }

    private ScoreMap getLongestLosingStreaks(List<Game> easyGames, List<Game> mediumGames, List<Game> hardGames) {
        Map<String, Double> easyStreaks = getStreaks(easyGames);
        Map<String, Double> mediumStreaks = getStreaks(mediumGames);
        Map<String, Double> hardStreaks = getStreaks(hardGames);


        return new ScoreMap(easyStreaks.get("lose"), mediumStreaks.get("lose"), hardStreaks.get("lose"));
    }

    private Map<String, Double> getStreaks(List<Game> games) {
        Map<String, Double> streaks = new HashMap<>();

        List<Double> winningStreaks = new ArrayList<>();
        List<Double> losingStreaks = new ArrayList<>();

        double winningStreak = 0;
        double losingStreak = 0;

        for (Game game : games) {
            if (game.isSuccess()) {
                winningStreak++;
                if (losingStreak != 0) {
                    losingStreaks.add(losingStreak);
                    losingStreak = 0;
                }
                continue;
            }

            losingStreak++;
            if (winningStreak != 0) {
                winningStreaks.add(winningStreak);
                winningStreak = 0;
            }
        }

        if (winningStreak != 0) {
            winningStreaks.add(winningStreak);
        }

        if (losingStreak != 0) {
            losingStreaks.add(losingStreak);
        }


        streaks.put("win", !winningStreaks.isEmpty() ? Collections.max(winningStreaks) : 0.0);
        streaks.put("lose", !losingStreaks.isEmpty() ? Collections.max(losingStreaks) : 0.0);

        return streaks;
    }
}
