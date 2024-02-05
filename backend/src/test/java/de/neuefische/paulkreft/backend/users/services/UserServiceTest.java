package de.neuefische.paulkreft.backend.users.services;

import de.neuefische.paulkreft.backend.github.services.GithubService;
import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.services.TimeService;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.repositories.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;

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
        userService = new UserService(usersRepo, idService, timeService, githubService);

        testUser = new User("Some UUID", "Some Name", "someemail@soem.de", "", Instant.now(), Instant.now());
    }

    @Test
    void getUserShouldReturnNullWhenUserIsNull() {
        // When
        User result = userService.getUser(null, null, null);

        // Then
        assertNull(result);
        verifyNoInteractions(usersRepo);
    }

    @Test
    void getUserShouldReturnNullWhenEmailIsNull() {
        // Given
        OAuth2User user = mock(OAuth2User.class);
        when(githubService.getUserEmail(Mockito.any(), Mockito.any())).thenReturn(null);

        // When
        Executable executable =
                () -> userService.getUser(user, null, null);

        // Then
        assertThrows(RuntimeException.class, executable);
        verify(githubService, times(1)).getUserEmail(Mockito.any(), Mockito.any());
    }

    @Test
    void getUserShouldSaveNewUserWhenUserNotInDatabase() {
        // Given
        OAuth2User user = mock(OAuth2User.class);

        when(user.getAttribute("name")).thenReturn(testUser.name());
        when(idService.generateUUID()).thenReturn(testUser.id());
        when(githubService.getUserEmail(Mockito.any(), Mockito.any())).thenReturn(testUser.email());
        when(githubService.getUserEmail(Mockito.any(), Mockito.any())).thenReturn("someemail@soem.de");

        when(usersRepo.existsUserByEmail(testUser.email())).thenReturn(false);
        when(usersRepo.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.getUser(user, null, null);

        // Then
        assertNotNull(result);
        assertEquals(testUser.id(), result.id());
        assertEquals(testUser.email(), result.email());
        verify(usersRepo, times(1)).existsUserByEmail(testUser.email());
        verify(usersRepo, times(1)).save(any(User.class));
        verify(idService, times(1)).generateUUID();
        verify(user, times(1)).getAttribute(Mockito.any());
        verify(githubService, times(1)).getUserEmail(Mockito.any(), Mockito.any());
        verifyNoMoreInteractions(usersRepo, idService, githubService);
    }

    @Test
    void getUserShouldReturnExistingUserWhenUserInDatabase() {
        // Given
        OAuth2User user = mock(OAuth2User.class);
        Instant now = Instant.parse("2016-06-09T00:00:00.00Z");
        when(timeService.getNow()).thenReturn(now);
        when(githubService.getUserEmail(Mockito.any(), Mockito.any())).thenReturn("someemail@soem.de");

        when(user.getAttribute("login")).thenReturn(testUser.name());
        when(usersRepo.existsUserByEmail(testUser.email())).thenReturn(true);
        when(usersRepo.findUserByEmail(testUser.email())).thenReturn(testUser);
        when(usersRepo.save(Mockito.any(User.class))).thenReturn(testUser);

        // When
        User result = userService.getUser(user, null, null);

        // Then
        assertNotNull(result);
        assertEquals(testUser.id(), result.id());
        assertEquals(testUser.email(), result.email());
        assertEquals(testUser.name(), result.name());
        verify(usersRepo, times(1)).existsUserByEmail(testUser.email());
        verify(usersRepo, times(1)).findUserByEmail(testUser.email());
        verify(usersRepo, times(1)).save(Mockito.any(User.class));
        verify(user, times(1)).getAttribute(Mockito.any());
        verify(timeService, times(1)).getNow();
        verify(githubService, times(1)).getUserEmail(Mockito.any(), Mockito.any());
        verifyNoMoreInteractions(idService, usersRepo, timeService, user, githubService);
    }
}
