package de.neuefische.paulkreft.backend.security.controller;

import de.neuefische.paulkreft.backend.user.model.User;
import de.neuefische.paulkreft.backend.user.repository.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SignUpControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UsersRepo usersRepo;

    User testUser;

    @BeforeEach
    public void instantiateTestUser() {
        Instant now = Instant.parse("2016-06-09T00:00:00.00Z");
        testUser = new User("123", "Paul", "testemail@at.de", "", now, now);
    }


    @DirtiesContext
    @Test
    void signUpWithEmailTest_whenEmailAlreadyRegistered_throwEmailAlreadyRegisteredException() throws Exception {
        // Given
        usersRepo.save(testUser);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/signup/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "email": "testemail@at.de",
                                    "password": "123%5Tsas"
                                 }
                                """))

                // Then
                .andExpect(status().isConflict())
                .andExpect(content().string(""));
    }

    @Test
    void signUpWithEmailTest_whenPasswordInvalid_throwIllegalArgumentException() throws Exception {
        // Given & When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/signup/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "email": "testemail@at.de",
                                    "password": ""
                                 }
                                """))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }
}