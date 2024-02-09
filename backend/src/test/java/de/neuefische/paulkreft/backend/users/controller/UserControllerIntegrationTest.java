package de.neuefische.paulkreft.backend.users.controller;

import de.neuefische.paulkreft.backend.github.services.GithubService;
import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.services.TimeService;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.repository.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepo usersRepo;

    @MockBean
    private IdService idService;

    @MockBean
    private TimeService timeService;

    @MockBean
    private GithubService githubService;


    private User testUser;

    @BeforeEach
    public void instantiateTestUser() {
        Instant now = Instant.parse("2016-06-09T00:00:00.00Z");
        testUser = new User("123", "Paul", "testemail@at.de", "", now, now);
    }

    @Test
    void testGetUser_whenNotLoggedIn_returnNull() throws Exception {
        // Given, when and then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @DirtiesContext
    @Test
    void testGetUser_whenReturningUserIsLoggedIn_returnReturningUser() throws Exception {
        usersRepo.save(testUser);

        // Given
        Instant now = Instant.parse("2016-06-09T00:00:00Z");
        when(timeService.getNow()).thenReturn(now);
        when(idService.generateUUID()).thenReturn("123");
        when(githubService.getUserEmail(Mockito.any(),Mockito.any())).thenReturn("testemail@at.de");

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .with(oidcLogin().userInfoToken(token -> token.claim("login", "Paul"))))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id":"123",
                            "email":"testemail@at.de",
                            "name":"Paul",
                            "lastActive": "2016-06-09T00:00:00Z",
                            "createdAt": "2016-06-09T00:00:00Z"
                        }
                        """));


        Mockito.verify(timeService, Mockito.times(1)).getNow();
        Mockito.verify(githubService, Mockito.times(1)).getUserEmail(Mockito.any(),Mockito.any());
        verifyNoMoreInteractions(timeService, githubService);
    }

    @Test
    void testGetUser_whenNewUserIsLoggedIn_returnNewUser() throws Exception {
        // Given
        Instant now = Instant.parse("2016-06-09T00:00:00Z");

        when(idService.generateUUID()).thenReturn("123");
        when(timeService.getNow()).thenReturn(now);
        when(githubService.getUserEmail(Mockito.any(),Mockito.any())).thenReturn("testemail@at.de");

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .with(oidcLogin().userInfoToken(token -> token.claim("login", "Paul"))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id":"123",
                            "email":"testemail@at.de",
                            "name":"testemail@at.de",
                            "lastActive": "2016-06-09T00:00:00Z",
                            "createdAt": "2016-06-09T00:00:00Z"
                        }
                        """));

        // Then
        Mockito.verify(idService, Mockito.times(1)).generateUUID();
        Mockito.verify(timeService, Mockito.times(1)).getNow();
        Mockito.verify(githubService, Mockito.times(1)).getUserEmail(Mockito.any(),Mockito.any());
        verifyNoMoreInteractions(idService, timeService, githubService);
    }

    @DirtiesContext
    @Test
    void testGetUser_whenGithubEmailNotFound_throwGithubEmailNotFoundException() throws Exception {
        usersRepo.save(testUser);

        // Given
        Instant now = Instant.parse("2016-06-09T00:00:00Z");
        when(timeService.getNow()).thenReturn(now);
        when(idService.generateUUID()).thenReturn("123");
        when(githubService.getUserEmail(Mockito.any(),Mockito.any())).thenReturn(null);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .with(oidcLogin().userInfoToken(token -> token.claim("login", "Paul"))))
                // Then
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));


        Mockito.verify(githubService, Mockito.times(1)).getUserEmail(Mockito.any(),Mockito.any());
        verifyNoMoreInteractions(timeService, githubService);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    void testGetUser_whenEmailNotInDB_returnNull() throws Exception {
        // Given & When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))

                // Then
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verifyNoMoreInteractions(timeService, githubService);
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "testemail@at.de", password = "pwd", roles = "USER")
    void testGetUser_whenReturningEmailUserIsLoggedIn_returnReturningUser() throws Exception {
        usersRepo.save(testUser);

        // Given
        Instant now = Instant.parse("2016-06-09T00:00:00Z");
        when(timeService.getNow()).thenReturn(now);
        when(githubService.getUserEmail(Mockito.any(),Mockito.any())).thenReturn("testemail@at.de");

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {"id":"123",
                                "name":"Paul",
                                "email":"testemail@at.de",
                                "lastActive":"2016-06-09T00:00:00Z",
                                "createdAt":"2016-06-09T00:00:00Z"
                                }
                                """));


        Mockito.verify(timeService, Mockito.times(1)).getNow();
        verifyNoMoreInteractions(timeService, githubService);
    }
}
