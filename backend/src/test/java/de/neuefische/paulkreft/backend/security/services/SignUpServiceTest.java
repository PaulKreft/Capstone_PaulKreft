package de.neuefische.paulkreft.backend.security.services;

import de.neuefische.paulkreft.backend.security.models.SignUpRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SignUpServiceTest {

    @Test
    void signUpWithEmailTest_whenValidEmailAndPassword_ReturnEmail() {
        // Given
        SignUpService signUpService = new SignUpService();
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "V!Qzx^sgSNe9XnJ8wTkem");

        String expected = "example@domain.com";

        // When
        String actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenEmailWithoutAtSign_ReturnNull() {
        // Given
        SignUpService signUpService = new SignUpService();
        SignUpRequest signUpRequest = new SignUpRequest("exampledomain.com", "V!Qzx^sgSNe9XnJ8wTkem");

        // When
        String actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordEmptyString_ReturnNull() {
        // Given
        SignUpService signUpService = new SignUpService();
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "");

        // When
        String actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordWithoutSpecialChars_ReturnNull() {
        // Given
        SignUpService signUpService = new SignUpService();
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "wCgqLfDaSW83eXkDsqV2U");

        // When
        String actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordWithoutUpperCaseLetter_ReturnNull() {
        // Given
        SignUpService signUpService = new SignUpService();
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "q$^38n9*dx5auh7r#g%cz");

        // When
        String actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordWithoutLowerCaseLetter_ReturnNull() {
        // Given
        SignUpService signUpService = new SignUpService();
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "678^SQ*R4Z*^DGB*LD*4B");

        // When
        String actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordWithoutNumber_ReturnNull() {
        // Given
        SignUpService signUpService = new SignUpService();
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "cTG&gXAHH%QCHNA#QrEHh");

        // When
        String actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }

    @Test
    void signUpWithEmailEmailTest_whenPasswordLessThanEightSigns_ReturnNull() {
        // Given
        SignUpService signUpService = new SignUpService();
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "fqX$7fe");

        // When
        String actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }


    @Test
    void signUpWithEmailEmailTest_whenPasswordMoreThanHundredSigns_ReturnNull() {
        // Given
        SignUpService signUpService = new SignUpService();
        SignUpRequest signUpRequest = new SignUpRequest("example@domain.com", "E#4&W%5X!8eyK@hnGu%p7MMznTF6Q#iT%7LVi4ZpaQ#Sqwr4Ud6ZJV7Kj#KHyiQbakCJt$PmkuLAqK!%9T8bn%qtk$hP6t^6DGvt6");

        // When
        String actual = signUpService.signUpWithEmail(signUpRequest);

        // Then
        assertNull(actual);
    }
}