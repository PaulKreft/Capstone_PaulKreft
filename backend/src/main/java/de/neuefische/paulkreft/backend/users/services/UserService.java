package de.neuefische.paulkreft.backend.users.services;

import de.neuefische.paulkreft.backend.exception.GithubEmailNotFoundException;
import de.neuefische.paulkreft.backend.game.model.Game;
import de.neuefische.paulkreft.backend.game.repository.GameRepo;
import de.neuefische.paulkreft.backend.github.services.GithubService;
import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.services.TimeService;
import de.neuefische.paulkreft.backend.users.models.ScoreMap;
import de.neuefische.paulkreft.backend.users.models.Statistics;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.models.UserGet;
import de.neuefische.paulkreft.backend.users.repository.UsersRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepo usersRepo;
    private final GameRepo gameRepo;
    private final IdService idService;
    private final TimeService timeService;
    private final GithubService githubService;

    public UserGet getLoggedInUser(Principal user, HttpServletRequest request) {
        if (user == null) {
            return null;
        }

        if (user instanceof UsernamePasswordAuthenticationToken) {
            return getEmailUser(user);
        }

        // If type is OAuth2AuthenticationToken
        return getOAuthUser(request);
    }

    private UserGet getEmailUser(Principal user) {
        String email = user.getName();

        if (!existsByEmail(email)) {
            return null;
        }

        User currentUser = usersRepo.findUserByEmail(email);

        return updateUser(currentUser.withLastActive(timeService.getNow()));
    }

    private UserGet getOAuthUser(HttpServletRequest request) {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        OAuth2AuthenticationToken auth =
                (OAuth2AuthenticationToken) authentication;

        String email = githubService.getUserEmail(request, auth);

        if (email == null) {
            throw new GithubEmailNotFoundException("Could not get GitHub email!");
        }

        boolean isReturningUser = usersRepo.existsUserByEmail(email);

        Instant now = timeService.getNow();
        if (!isReturningUser) {
            return new UserGet(usersRepo.save(new User(idService.generateUUID(), email, email, null, now, now)));
        }

        User currentUser = usersRepo.findUserByEmail(email);

        return updateUser(currentUser.withLastActive(now));
    }

    public UserGet createUser(String name, String email, String password) {
        Instant now = timeService.getNow();

        return new UserGet(usersRepo.save(new User(idService.generateUUID(), name, email, password, now, now)));
    }

    public UserGet updateUser(User user) {
        return new UserGet(usersRepo.save(user));
    }

    public boolean existsByEmail(String email) {
        return usersRepo.existsUserByEmail(email);
    }

    public Statistics getStatistics(String id) {
        List<Game> allGames = gameRepo.findAllByUserIdOrderByCreatedAtAsc(id);

        List<Game> easyGames = allGames.stream().filter(game -> game.difficulty() == 1).toList();
        List<Game> mediumGames = allGames.stream().filter(game -> game.difficulty() == 2).toList();
        List<Game> hardGames = allGames.stream().filter(game -> game.difficulty() == 4).toList();

        Map<String, Double> easyStreaks = getStreaks(easyGames);
        Map<String, Double> mediumStreaks = getStreaks(mediumGames);
        Map<String, Double> hardStreaks = getStreaks(hardGames);


        List<Double> easyDurations = easyGames.stream().map(game -> Double.valueOf(game.duration())).filter(d -> d != 0).toList();
        List<Double> mediumDurations = mediumGames.stream().map(game -> Double.valueOf(game.duration())).filter(d -> d != 0).toList();
        List<Double> hardDurations = hardGames.stream().map(game -> Double.valueOf(game.duration())).filter(d -> d != 0).toList();

        Double fastestSolveEasy = !easyDurations.isEmpty() ? easyDurations.getFirst() : null;
        Double fastestSolveMedium = !mediumDurations.isEmpty() ? mediumDurations.getFirst() : null;
        Double fastestSolveHard = !hardDurations.isEmpty() ? hardDurations.getFirst() : null;

        Double averageDurationEasy = !easyDurations.isEmpty() ? easyDurations.stream().mapToDouble(v -> v).sum() / easyDurations.size() : null;
        Double averageDurationMedium = !mediumDurations.isEmpty() ? mediumDurations.stream().mapToDouble(v -> v).sum() : null;
        Double averageDurationHard = !hardDurations.isEmpty() ? hardDurations.stream().mapToDouble(v -> v).sum() : null;

        ScoreMap longestWinningStreaks = new ScoreMap(easyStreaks.getOrDefault("win", null), mediumStreaks.getOrDefault("win", null), hardStreaks.getOrDefault("win", null));
        ScoreMap longestLosingStreaks = new ScoreMap(easyStreaks.getOrDefault("lose", null), mediumStreaks.getOrDefault("lose", null), hardStreaks.getOrDefault("lose", null));
        ScoreMap totalGames = new ScoreMap((double) easyGames.size(), (double) mediumGames.size(), (double) hardGames.size());
        ScoreMap totalGamesWon = new ScoreMap((double) easyGames.stream().filter(Game::isSuccess).toList().size(), (double) mediumGames.stream().filter(Game::isSuccess).toList().size(), (double) hardGames.stream().filter(Game::isSuccess).toList().size());
        ScoreMap fastestSolve = new ScoreMap(fastestSolveEasy, fastestSolveMedium, fastestSolveHard);
        ScoreMap averageDuration = new ScoreMap(averageDurationEasy, averageDurationMedium, averageDurationHard);

        return new Statistics(longestWinningStreaks, longestLosingStreaks, totalGames, totalGamesWon, fastestSolve, averageDuration);
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


        streaks.put("win", !winningStreaks.isEmpty() ? Collections.max(winningStreaks) : null);
        streaks.put("lose", !losingStreaks.isEmpty() ? Collections.max(winningStreaks) : null);

        return streaks;
    }
}
