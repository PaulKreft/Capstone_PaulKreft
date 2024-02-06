package de.neuefische.paulkreft.backend.security.services;

import de.neuefische.paulkreft.backend.security.models.SignUpRequest;
import de.neuefische.paulkreft.backend.users.models.UserGet;
import de.neuefische.paulkreft.backend.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class SignUpServiceTest {
    UserService userService;
    SignUpService signUpService;

    UserGet testUser;

    @BeforeEach
    public void instantiateServices() {
        userService = Mockito.mock(UserService.class);
        signUpService = new SignUpService(userService);

        testUser = new UserGet("1", "Soso", "example@domain.com", Instant.now(), Instant.now());
    }

    @Test
    void signUpWithEmailTest_whenValidEmailAndPassword_ReturnEmail() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "V!Qzx^sgSNe9XnJ8wTkem");
        when(userService.existsByEmail("example@domain.com")).thenReturn(false);
        when(userService.createUser(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(testUser);

        String expected = "example@domain.com";

        // When
        UserGet actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertEquals(expected, actual.email());
    }

    @Test
    void signUpWithEmailTest_whenEmailAlreadyRegistered_ReturnNull() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "E#4&W%5X!8eyK@hnGu%p7MMznTF6Q#iT%7LVi4ZpaQ#Sqwr4Ud6ZJV7Kj#KHyiQbakCJt$PmkuLAqK!%9T8bn%qtk$hP6t^6DGvt6");
        when(userService.existsByEmail("example@domain.com")).thenReturn(true);

        // When
        UserGet actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    static SignUpRequest[] invalidSignUpRequests() {
        return new SignUpRequest[]{
                // Email without @
                new SignUpRequest("exampledomain.com", "V!Qzx^sgSNe9XnJ8wTkem"),
                // Password is empty string
                new SignUpRequest("example@domain.com", ""),
                // Password has no special characters
                new SignUpRequest("example@domain.com", "wCgqLfDaSW83eXkDsqV2U"),
                // Password has no upper case letter
                new SignUpRequest("example@domain.com", "q$^38n9*dx5auh7r#g%cz"),
                // Password has no lower case letter
                new SignUpRequest("example@domain.com", "678^SQ*R4Z*^DGB*LD*4B"),
                // Password has no number
                new SignUpRequest("example@domain.com", "cTG&gXAHH%QCHNA#QrEHh"),
                // Password is too short
                new SignUpRequest("example@domain.com", "fqX$7fe"),
                // Password is too long
                new SignUpRequest("example@domain.com", "E#4&W%5X!8eyK@hnGu%p7MMznTF6Q#iT%7LVi4ZpaQ#Sqwr4Ud6ZJV7Kj#KHyiQbakCJt$PmkuLAqK!%9T8bn%qtk$hP6t^6DGvt6")
        };
    }

    @ParameterizedTest
    @MethodSource("invalidSignUpRequests")
    void signUpWithEmailTest_invalidSignUps(SignUpRequest signUpRequest) {
        assertNull(signUpService.signUpWithEmail(signUpRequest));
    }
}