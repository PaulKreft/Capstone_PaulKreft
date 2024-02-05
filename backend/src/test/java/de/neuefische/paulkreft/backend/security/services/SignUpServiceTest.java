package de.neuefische.paulkreft.backend.security.services;

import de.neuefische.paulkreft.backend.security.models.SignUpRequest;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class SignUpServiceTest {
    UserService userService;
    SignUpService signUpService;

    User testUser;

    @BeforeEach
    public void instantiateServices() {
        userService = Mockito.mock(UserService.class);
        signUpService = new SignUpService(userService);

        testUser = new User("1", "Soso", "example@domain.com", "", Instant.now(), Instant.now());
    }

    @Test
    void signUpWithEmailTest_whenValidEmailAndPassword_ReturnEmail() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "V!Qzx^sgSNe9XnJ8wTkem");
        when(userService.existsByEmail("example@domain.com")).thenReturn(false);
        when(userService.createUser(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(testUser);

        String expected = "example@domain.com";

        // When
        User actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertEquals(expected, actual.email());
    }

    @Test
    void signUpWithEmailEmailTest_whenEmailWithoutAtSign_ReturnNull() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("exampledomain.com", "V!Qzx^sgSNe9XnJ8wTkem");

        // When
        User actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordEmptyString_ReturnNull() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "");

        // When
        User actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordWithoutSpecialChars_ReturnNull() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "wCgqLfDaSW83eXkDsqV2U");

        // When
        User actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordWithoutUpperCaseLetter_ReturnNull() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "q$^38n9*dx5auh7r#g%cz");

        // When
        User actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordWithoutLowerCaseLetter_ReturnNull() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "678^SQ*R4Z*^DGB*LD*4B");

        // When
        User actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordWithoutNumber_ReturnNull() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "cTG&gXAHH%QCHNA#QrEHh");

        // When
        User actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordLessThanEightSigns_ReturnNull() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "fqX$7fe");

        // When
        User actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordMoreThanHundredSigns_ReturnNull() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "E#4&W%5X!8eyK@hnGu%p7MMznTF6Q#iT%7LVi4ZpaQ#Sqwr4Ud6ZJV7Kj#KHyiQbakCJt$PmkuLAqK!%9T8bn%qtk$hP6t^6DGvt6");

        // When
        User actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenEmailAlreadyRegistered_ReturnNull() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "E#4&W%5X!8eyK@hnGu%p7MMznTF6Q#iT%7LVi4ZpaQ#Sqwr4Ud6ZJV7Kj#KHyiQbakCJt$PmkuLAqK!%9T8bn%qtk$hP6t^6DGvt6");
        when(userService.existsByEmail("example@domain.com")).thenReturn(true);

        // When
        User actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }
}