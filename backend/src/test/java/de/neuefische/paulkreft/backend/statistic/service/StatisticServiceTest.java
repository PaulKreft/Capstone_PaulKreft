package de.neuefische.paulkreft.backend.statistic.service;

import de.neuefische.paulkreft.backend.game.classic.model.ClassicGame;
import de.neuefische.paulkreft.backend.game.classic.repository.GameRepo;
import de.neuefische.paulkreft.backend.statistic.model.ScoreMap;
import de.neuefische.paulkreft.backend.statistic.model.ClassicStatistics;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class StatisticServiceTest {
    @Test
    void getUserStatisticsTest_whenAllStatisticsCalculable_returnCorrectValues() {
        // Given
        GameRepo gameRepo = Mockito.mock(GameRepo.class);

        StatisticService statisticService = new StatisticService(gameRepo);


        ClassicGame game1 = new ClassicGame("7b6cc6c8-b98f-428a-bee5-2e4e804901cd", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, false, 0, List.of("#f1da9b", "#bb463d", "#0e2564", "#237dc9", "#44b9c2", "#dc8236"), Instant.parse("2024-02-09T15:18:59.426Z"));
        ClassicGame game2 = new ClassicGame("51e74bcd-d341-4ac9-8311-2513a86e9261", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, true, 8515, List.of("#05a928", "#adbb2f", "#0c58b8", "#5244d0", "#f3a747", "#fa56d7"), Instant.parse("2024-02-09T15:19:09.388Z"));
        ClassicGame game3 = new ClassicGame("b6bd5d8b-42c6-4efc-985f-dfc48e2df7e0", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, false, 0, List.of("#53accc", "#ac5333", "#3ed3ab", "#c12c54", "#6a13b4", "#95ec4b"), Instant.parse("2024-02-09T15:19:13.671Z"));
        ClassicGame game4 = new ClassicGame("25fefbca-6d74-408f-aaa3-397e37a19764", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, true, 5000, List.of("#4a4b01", "#b5b4fe", "#7d8b42", "#960ba7", "#8274bd", "#69f458"), Instant.parse("2024-02-09T15:19:23.261Z"));
        ClassicGame game5 = new ClassicGame("641730f8-0654-4912-ae4f-37bbd64d4a86", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, true, 16616, List.of("#099c93", "#c2a995", "#c51a5f", "#f6636c", "#3d566a", "#3ae5a0"), Instant.parse("2024-02-09T16:13:23.514Z"));
        ClassicGame game6 = new ClassicGame("066cfbb8-c4f4-45d8-9af5-e65970e11f5c", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 2, true, 8578, List.of("#8e4392", "#9532a1", "#8e1e9e", "#3e6697", "#71bc6d", "#c19968", "#6acd5e", "#71e161"), Instant.parse("2024-02-09T16:13:37.306Z"));
        ClassicGame game7 = new ClassicGame("11b7aabb-2261-48a0-8ce0-1c30d7d13dc8", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 2, true, 16699, List.of("#1d9dbd", "#c32605", "#3cd9fa", "#e26242", "#177ca5", "#ea622e", "#e8835a", "#159dd1"), Instant.parse("2024-02-09T16:13:55.319Z"));
        ClassicGame game8 = new ClassicGame("8fa85a56-1a78-48ff-b5f1-172f75475b0a", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 4, false, 0, List.of("#de02b8", "#60317a", "#5ce92d", "#9fce85", "#e67e12", "#e254b5", "#1981ed", "#227da2", "#a316d2", "#dd825d", "#21fd47", "#1dab4a"), Instant.parse("2024-02-09T16:14:15.221Z"));
        ClassicGame game9 = new ClassicGame("c15f9b9a-71d9-49e4-82e4-cbb9007358af", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 4, false, 0, List.of("#c19615", "#efde72", "#4f40fb", "#e26b51", "#3e69ea", "#5ee08c", "#5f3394", "#1d94ae", "#10218d", "#a0cc6b", "#a11f73", "#b0bf04"), Instant.parse("2024-02-09T16:14:25.192Z"));
        ClassicGame game10 = new ClassicGame("3cd692e8-f028-4e72-a92d-ed69dd4c6a94", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 4, false, 0, List.of("#9b01c2", "#5450f6", "#2a0cb1", "#195378", "#fd1b5b", "#02e4a4", "#d5f34e", "#77f5fc", "#e6ac87", "#64fe3d", "#abaf09", "#880a03"), Instant.parse("2024-02-09T16:14:49.157Z"));
        ClassicGame game11 = new ClassicGame("efdb3e28-78fa-4abe-9328-df77cfeb1186", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 4, false, 0, List.of("#1fb50d", "#855303", "#c542b0", "#1177e3", "#620463", "#986046", "#3abd4f", "#e04af2", "#9dfb9c", "#ee881c", "#679fb9", "#7aacfc"), Instant.parse("2024-02-09T16:14:53.990Z"));
        ClassicGame game12 = new ClassicGame("b91f14e0-086e-402b-9823-16a55278f713", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 4, true, 15833, List.of("#84211c", "#7bdee3", "#350e34", "#762766", "#8cbf63", "#be7b12", "#caf1cb", "#73409c", "#89d899", "#a773ff", "#4184ed", "#588c00"), Instant.parse("2024-02-09T16:15:10.996Z"));
        ClassicGame game13 = new ClassicGame("42e62761-2c95-488a-885b-ee2c012ab1dc", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, false, 0, List.of("#75ce6b", "#5dbaf4", "#a2450b", "#c65e86", "#39a179", "#8a3194"), Instant.parse("2024-02-09T16:16:53.418Z"));
        ClassicGame game14 = new ClassicGame("26c84fe4-b77b-4858-83e1-5a3a4499fabe", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, true, 3851, List.of("#1b43bc", "#023e44", "#fdc1bb", "#e4bc43", "#5c8731", "#a378ce"), Instant.parse("2024-02-09T16:16:58.237Z"));
        ClassicGame game15 = new ClassicGame("42ede063-4bcd-4e14-a091-80ab6a98285f", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, false, 0, List.of("#aba965", "#37e2c7", "#1fe8eb", "#e01714", "#54569a", "#c81d38"), Instant.parse("2024-02-09T16:17:05.101Z"));
        ClassicGame game16 = new ClassicGame("16c3ba8c-c7f4-4089-8719-2d3d5b8bab99", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, true, 3750, List.of("#74f523", "#3a928c", "#06085c", "#8b0adc", "#c56d73", "#f9f7a3"), Instant.parse("2024-02-09T16:17:10.384Z"));
        ClassicGame game17 = new ClassicGame("60aa46f9-feaf-4ca4-99b1-a6685a3712d7", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, false, 0, List.of("#5efe36", "#e93efa", "#16c105", "#a101c9", "#a5a045", "#5a5fba"), Instant.parse("2024-02-09T16:17:12.851Z"));
        ClassicGame game18 = new ClassicGame("8bc70a24-2e44-4b78-a333-8e6a486e8f21", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, true, 3632, List.of("#8ed974", "#d00308", "#71268b", "#2ffcf7", "#095bf0", "#f6a40f"), Instant.parse("2024-02-09T16:17:17.570Z"));
        ClassicGame game19 = new ClassicGame("0c88bce7-4232-4f1f-8bdc-013658874091", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 1, true, 6700, List.of("#8bd278", "#67dd65", "#9abd98", "#98229a", "#742d87", "#654267"), Instant.parse("2024-02-09T16:17:25.085Z"));
        ClassicGame game20 = new ClassicGame("04fe2411-6be3-4d92-b5a8-06d8386f0cc1", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 4, false, 0, List.of("#000000", "#ffffff", "#000000", "#ffffff", "#000000", "#ffffff", "#000000", "#ffffff", "#000000", "#ffffff", "#000000", "#ffffff"), Instant.parse("2024-02-09T16:23:37.832Z"));
        ClassicGame game21 = new ClassicGame("7824747f-97d4-415f-a6f4-080906b78c79", "f39e6614-8132-4f8e-bd98-6fdf50fed3b0", "", 2, false, 0, List.of("#695cdd", "#522f88", "#96a322", "#add077", "#f56853", "#d6656c", "#299a93", "#0a97ac"), Instant.parse("2024-02-09T16:25:45.317Z"));

        List<ClassicGame> games = List.of(game1, game2, game3, game4, game5, game6, game7, game8, game9, game10, game11, game12, game13, game14, game15, game16, game17, game18, game19, game20, game21);

        when(gameRepo.findAllByUserIdOrderByCreatedAtAsc(any())).thenReturn(games);

        ClassicStatistics expected = new ClassicStatistics(
                new ScoreMap(2.0, 2.0, 1.0),
                new ScoreMap(1.0, 1.0, 4.0),
                new ScoreMap(12.0, 3.0, 6.0),
                new ScoreMap(7.0, 2.0, 1.0),
                new ScoreMap(3632.0, 8578.0, 15833.0),
                new ScoreMap(6866.285714285715, 25277.0, 15833.0)
        );

        // When
        ClassicStatistics actual = statisticService.getUserStatistics("");

        // Then
        assertNotNull(actual);
        assertEquals(expected.longestWinningStreak().easy(), actual.longestWinningStreak().easy());
        assertEquals(expected.longestWinningStreak().medium(), actual.longestWinningStreak().medium());
        assertEquals(expected.longestWinningStreak().hard(), actual.longestWinningStreak().hard());

        assertEquals(expected.longestLosingStreak().easy(), actual.longestLosingStreak().easy());
        assertEquals(expected.longestLosingStreak().medium(), actual.longestLosingStreak().medium());
        assertEquals(expected.longestLosingStreak().hard(), actual.longestLosingStreak().hard());

        assertEquals(expected.gamesPlayed().easy(), actual.gamesPlayed().easy());
        assertEquals(expected.gamesPlayed().medium(), actual.gamesPlayed().medium());
        assertEquals(expected.gamesPlayed().hard(), actual.gamesPlayed().hard());

        assertEquals(expected.gamesWon().easy(), actual.gamesWon().easy());
        assertEquals(expected.gamesWon().medium(), actual.gamesWon().medium());
        assertEquals(expected.gamesWon().hard(), actual.gamesWon().hard());

        assertEquals(expected.fastestSolve().easy(), actual.fastestSolve().easy());
        assertEquals(expected.fastestSolve().medium(), actual.fastestSolve().medium());
        assertEquals(expected.fastestSolve().hard(), actual.fastestSolve().hard());

        assertEquals(expected.averageTime().easy(), actual.averageTime().easy());
        assertEquals(expected.averageTime().medium(), actual.averageTime().medium());
        assertEquals(expected.averageTime().hard(), actual.averageTime().hard());

        assertEquals(expected, actual);
        verify(gameRepo, times(1)).findAllByUserIdOrderByCreatedAtAsc(any());
        verifyNoMoreInteractions(gameRepo);
    }

}