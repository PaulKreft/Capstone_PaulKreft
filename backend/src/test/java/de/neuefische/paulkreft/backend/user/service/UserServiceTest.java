package de.neuefische.paulkreft.backend.user.service;

import de.neuefische.paulkreft.backend.github.service.GithubService;
import de.neuefische.paulkreft.backend.statistic.service.StatisticService;
import de.neuefische.paulkreft.backend.utils.service.IdService;
import de.neuefische.paulkreft.backend.utils.service.TimeService;
import de.neuefische.paulkreft.backend.user.model.User;
import de.neuefische.paulkreft.backend.user.model.UserGet;
import de.neuefische.paulkreft.backend.user.repository.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.security.Principal;
import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UsersRepo usersRepo;

    private IdService idService;

    private TimeService timeService;

    private UserService userService;

    private GithubService githubService;

    private User testUser;

    @BeforeEach
    public void instantiateTestUser() {
        usersRepo = Mockito.mock(UsersRepo.class);
        idService = Mockito.mock(IdService.class);
        timeService = Mockito.mock(TimeService.class);
        githubService = Mockito.mock(GithubService.class);
        userService = new UserService(usersRepo, Mockito.mock(StatisticService.class), idService, timeService, githubService);

        testUser = new User("Some UUID", "Some Name", "someemail@soem.de", "", Instant.now(), Instant.now());
    }

    @Test
    void getUserShouldReturnNullWhenUserIsNull() {
        // When
        UserGet result = userService.getLoggedInUser(null, null);

        // Then
        assertNull(result);
        verifyNoInteractions(usersRepo);
    }

    @Test
    void getUserShouldReturnNullWhenEmailIsNull() {
        // Given
        Principal user = mock(Principal.class);
        when(githubService.getUserEmail(Mockito.any(), Mockito.any())).thenReturn(null);

        // When
        Executable executable =
                () -> userService.getLoggedInUser(user, null);

        // Then
        assertThrows(RuntimeException.class, executable);
        verify(githubService, times(1)).getUserEmail(Mockito.any(), Mockito.any());
    }

    @Test
    void getUserShouldSaveNewUserWhenUserNotInDatabase() {
        // Given
        Principal user = mock(Principal.class);

        when(user.getName()).thenReturn(testUser.email());
        when(idService.generateUUID()).thenReturn(testUser.id());
        when(githubService.getUserEmail(Mockito.any(), Mockito.any())).thenReturn(testUser.email());
        when(githubService.getUserEmail(Mockito.any(), Mockito.any())).thenReturn("someemail@soem.de");

        when(usersRepo.existsUserByEmail(testUser.email())).thenReturn(false);
        when(usersRepo.save(any(User.class))).thenReturn(testUser);

        // When
        UserGet result = userService.getLoggedInUser(user, null);

        // Then
        assertNotNull(result);
        assertEquals(testUser.id(), result.id());
        assertEquals(testUser.email(), result.email());
        verify(usersRepo, times(1)).existsUserByEmail(testUser.email());
        verify(usersRepo, times(1)).save(any(User.class));
        verify(idService, times(1)).generateUUID();
        verify(githubService, times(1)).getUserEmail(Mockito.any(), Mockito.any());
        verifyNoMoreInteractions(usersRepo, idService, githubService);
    }

    @Test
    void getUserShouldReturnExistingUserWhenUserInDatabase() {
        // Given
        Principal user = mock(Principal.class);
        Instant now = Instant.parse("2016-06-09T00:00:00.00Z");
        when(timeService.getNow()).thenReturn(now);
        when(githubService.getUserEmail(Mockito.any(), Mockito.any())).thenReturn("someemail@soem.de");

        when(user.getName()).thenReturn(testUser.email());
        when(usersRepo.existsUserByEmail(testUser.email())).thenReturn(true);
        when(usersRepo.findUserByEmail(testUser.email())).thenReturn(testUser);
        when(usersRepo.save(Mockito.any(User.class))).thenReturn(testUser);

        // When
        UserGet result = userService.getLoggedInUser(user, null);

        // Then
        assertNotNull(result);
        assertEquals(testUser.id(), result.id());
        assertEquals(testUser.email(), result.email());
        assertEquals(testUser.name(), result.name());
        verify(usersRepo, times(1)).existsUserByEmail(testUser.email());
        verify(usersRepo, times(1)).findUserByEmail(testUser.email());
        verify(usersRepo, times(1)).save(Mockito.any(User.class));
        verify(timeService, times(1)).getNow();
        verify(githubService, times(1)).getUserEmail(Mockito.any(), Mockito.any());
        verifyNoMoreInteractions(idService, usersRepo, timeService, user, githubService);
    }

    @Test
    void createUserShouldReturnCreatedUser() {
        // Given
        Instant now = Instant.parse("2016-06-09T00:00:00.00Z");
        when(timeService.getNow()).thenReturn(now);
        when(idService.generateUUID()).thenReturn(testUser.id());
        when(usersRepo.save(Mockito.any(User.class))).thenReturn(testUser);

        String name = testUser.name();
        String email = testUser.email();
        String password = testUser.password();

        // When
        UserGet result = userService.createUser(name, email, password);

        // Then
        assertNotNull(result);
        assertEquals(testUser.id(), result.id());
        assertEquals(testUser.email(), result.email());
        assertEquals(testUser.name(), result.name());
        assertEquals(testUser.lastActive(), result.lastActive());
        assertEquals(testUser.createdAt(), result.createdAt());
        verify(usersRepo, times(1)).save(Mockito.any(User.class));
        verify(timeService, times(1)).getNow();
        verify(idService, times(1)).generateUUID();
        verifyNoMoreInteractions(idService, usersRepo, timeService, githubService);
    }

    @Test
    void existsByEmailTest_whenCheckExistingUser_returnTrue() {
        // Given
        when(usersRepo.existsUserByEmail(testUser.email())).thenReturn(true);

        // When
        boolean result = userService.existsByEmail(testUser.email());

        // Then
        assertTrue(result);
        verify(usersRepo, times(1)).existsUserByEmail(testUser.email());
        verifyNoMoreInteractions(idService, usersRepo, timeService, githubService);
    }

    @Test
    void existsByEmailTest_whenCheckNonExistingUser_returnFalse() {
        // Given
        String nonExistingEmail = "not@exists.com";
        when(usersRepo.existsUserByEmail(nonExistingEmail)).thenReturn(false);

        // When
        boolean result = userService.existsByEmail(nonExistingEmail);

        // Then
        assertFalse(result);
        verify(usersRepo, times(1)).existsUserByEmail(nonExistingEmail);
        verifyNoMoreInteractions(idService, usersRepo, timeService, githubService);
    }

    @Test
    void updateUserTest_whenUpdateUserGet_ReturnUserGet() {
        // Given
        when(usersRepo.save(testUser)).thenReturn(testUser);

        // When
        UserGet expected = new UserGet(testUser);
        UserGet actual = userService.updateUser(testUser);

        // Then
        assertEquals(expected, actual);
        verify(usersRepo, times(1)).save(testUser);
        verifyNoMoreInteractions(idService, usersRepo, timeService, githubService);
    }

    @ParameterizedTest
    @MethodSource("provideUsernames")
    void updateUserTest_whenInvalidUsername_throwIllegalArgumentException(String invalidUsername) {
        // Given
        when(usersRepo.save(testUser)).thenReturn(testUser);

        // When
        User updatedUser = testUser.withName(invalidUsername);
        Executable executable = () -> userService.updateUser(updatedUser);

        // Then
        assertThrows(IllegalArgumentException.class, executable);
        verifyNoMoreInteractions(idService, usersRepo, timeService, githubService);
    }

    private static Stream<String> provideUsernames() {
        return Stream.of(
                "NameWithSeventeen", // Too long
                "Name",              // Too short
                null                 // Null
        );
    }
}
