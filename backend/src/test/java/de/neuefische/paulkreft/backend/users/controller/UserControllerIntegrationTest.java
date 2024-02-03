package de.neuefische.paulkreft.backend.users.controller;

import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.services.TimeService;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.repositories.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersRepo usersRepo;

    @MockBean
    private IdService idService;

    @MockBean
    private TimeService timeService;

    private User testUser;

    @BeforeEach
    public void instantiateTestUser() {
        Instant now = Instant.parse("2016-06-09T00:00:00.00Z");
        testUser = new User("123", 123, "Paul", now, now);
    }

    @Test
    void testGetLoggedInUser_whenNotLoggedIn_returnNull() throws Exception {
        // Given, when and then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testGetLoggedInUser_whenReturningUserIsLoggedIn_returnReturningUser() throws Exception {
        // Given
        Instant now = Instant.parse("2016-06-09T00:00:00.00Z");
        when(timeService.getNow()).thenReturn(now);

        when(usersRepo.existsUserByGithubId(testUser.githubId())).thenReturn(true);
        when(usersRepo.findUserByGithubId(testUser.githubId())).thenReturn(testUser);
        when(usersRepo.save(Mockito.any(User.class))).thenReturn(testUser);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .with(oidcLogin().userInfoToken(token -> token.claim("login", "Paul").claim("id", 123))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id":"123",
                            "githubId":123,
                            "name":"Paul"
                        }
                        """));

        // Then
        Mockito.verify(usersRepo, Mockito.times(1)).existsUserByGithubId(testUser.githubId());
        Mockito.verify(usersRepo, Mockito.times(1)).findUserByGithubId(testUser.githubId());
        Mockito.verify(usersRepo, Mockito.times(1)).save(testUser);
        verifyNoMoreInteractions(usersRepo);
    }

    @Test
    void testGetLoggedInUser_whenNewUserIsLoggedIn_returnNewUser() throws Exception {
        // Given
        Instant now = Instant.parse("2016-06-09T00:00:00.00Z");

        when(usersRepo.existsUserByGithubId(testUser.githubId())).thenReturn(false);
        when(usersRepo.save(Mockito.any(User.class))).thenReturn(testUser);

        when(idService.generateUUID()).thenReturn("123");
        when(timeService.getNow()).thenReturn(now);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .with(oidcLogin().userInfoToken(token -> token.claim("login", "Paul").claim("id", 123))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id":"123",
                            "githubId":123,
                            "name":"Paul"
                        }
                        """));

        // Then
        Mockito.verify(usersRepo, Mockito.times(1)).existsUserByGithubId(testUser.githubId());
        Mockito.verify(usersRepo, Mockito.times(1)).save(testUser);
        Mockito.verify(idService, Mockito.times(1)).generateUUID();
        verifyNoMoreInteractions(usersRepo, idService);
    }

    @Test
    void testGetLoggedInUser_whenNoGithubId_returnNull() throws Exception {
        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .with(oidcLogin().userInfoToken(token -> token.claim("login", "Paul"))))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // Then
        verifyNoInteractions(usersRepo);
    }
}
