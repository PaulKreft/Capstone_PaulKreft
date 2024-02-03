package de.neuefische.paulkreft.backend.users.services;

import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.services.TimeService;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.repositories.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private User testUser;

    @BeforeEach
    public void instantiateTestUser() {
        usersRepo = Mockito.mock(UsersRepo.class);
        idService = Mockito.mock(IdService.class);
        timeService = Mockito.mock(TimeService.class);
        userService = new UserService(usersRepo, idService, timeService);

        testUser = new User("Some UUID", 12345, "Some Name", Instant.now(), Instant.now());
    }

    @Test
    void getUserShouldReturnNullWhenUserIsNull() {
        // When
        User result = userService.getUser(null);

        // Then
        assertNull(result);
        verifyNoInteractions(usersRepo);
    }

    @Test
    void getUserShouldSaveNewUserWhenUserNotInDatabase() {
        // Given
        OAuth2User user = mock(OAuth2User.class);

        when(user.getAttribute("id")).thenReturn(testUser.githubId());
        when(user.getAttribute("name")).thenReturn(testUser.name());
        when(usersRepo.existsUserByGithubId(testUser.githubId())).thenReturn(false);
        when(idService.generateUUID()).thenReturn(testUser.id());

        when(usersRepo.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.getUser(user);

        // Then
        assertNotNull(result);
        assertEquals(testUser.id(), result.id());
        assertEquals(testUser.githubId(), result.githubId());
        verify(usersRepo, times(1)).existsUserByGithubId(testUser.githubId());
        verify(idService, times(1)).generateUUID();
        verify(usersRepo, times(1)).save(any(User.class));
        verifyNoMoreInteractions(usersRepo, idService);
    }

    @Test
    void getUserShouldReturnExistingUserWhenUserInDatabase() {
        // Given
        OAuth2User user = mock(OAuth2User.class);
        when(user.getAttribute("id")).thenReturn(testUser.githubId());
        when(usersRepo.existsUserByGithubId(testUser.githubId())).thenReturn(true);
        when(user.getAttribute("name")).thenReturn(testUser.name());
        when(usersRepo.findUserByGithubId(testUser.githubId())).thenReturn(testUser);

        // When
        User result = userService.getUser(user);

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(usersRepo, times(1)).existsUserByGithubId(testUser.githubId());
        verify(usersRepo, times(1)).findUserByGithubId(testUser.githubId());
        verifyNoMoreInteractions(idService, usersRepo);
    }
}
