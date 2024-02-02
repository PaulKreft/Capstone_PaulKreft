package de.neuefische.paulkreft.backend.users.controller;

import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.repositories.UsersRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
        User returningUser = new User("123", 123, "Paul");
        when(usersRepo.existsUserByGithubId(returningUser.githubId())).thenReturn(true);
        when(usersRepo.findUserByGithubId(returningUser.githubId())).thenReturn(returningUser);

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
        Mockito.verify(usersRepo, Mockito.times(1)).existsUserByGithubId(returningUser.githubId());
        Mockito.verify(usersRepo, Mockito.times(1)).findUserByGithubId(returningUser.githubId());
        verifyNoMoreInteractions(usersRepo);
    }

    @Test
    void testGetLoggedInUser_whenNewUserIsLoggedIn_returnNewUser() throws Exception {
        // Given
        User newUser = new User("123", 123, "Paul");
        when(usersRepo.existsUserByGithubId(newUser.githubId())).thenReturn(false);
        when(usersRepo.save(Mockito.any(User.class))).thenReturn(newUser);

        when(idService.generateUUID()).thenReturn("123");

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
        Mockito.verify(usersRepo, Mockito.times(1)).existsUserByGithubId(newUser.githubId());
        Mockito.verify(usersRepo, Mockito.times(1)).save(newUser);
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
