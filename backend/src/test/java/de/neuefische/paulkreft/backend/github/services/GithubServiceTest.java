package de.neuefische.paulkreft.backend.github.services;

import de.neuefische.paulkreft.backend.exception.GithubEmailNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GithubServiceTest {
    private static MockWebServer mockWebServer;

    private static final OAuth2AuthorizedClientRepository authorizedClientRepository = Mockito.mock(OAuth2AuthorizedClientRepository.class);

    private static GithubService githubService;

    @BeforeAll
    public static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        githubService = new GithubService(authorizedClientRepository, mockWebServer.url("/").toString());
    }

    @AfterAll
    public static void cleanup() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getUserEmail_whenValidResponse_thenReturnEmail() {
        // Given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        OAuth2AuthenticationToken auth = Mockito.mock(OAuth2AuthenticationToken.class);

        when(authorizedClientRepository.loadAuthorizedClient(any(), any(), any())).thenReturn(null);

        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        [{
                          "email": "test@test.de"
                          }]
                        """)
                .addHeader("Content-Type", "application/json"));

        String expected = "test@test.de";

        // When
        String actual = githubService.getUserEmail(request, auth);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void getUserEmail_whenInNullResponse_thenThrowGithubEmailNotFoundException() {
        // Given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        OAuth2AuthenticationToken auth = Mockito.mock(OAuth2AuthenticationToken.class);

        when(authorizedClientRepository.loadAuthorizedClient(any(), any(), any())).thenReturn(null);

        mockWebServer.enqueue(new MockResponse()
                .setBody("")
                .addHeader("Content-Type", "application/json"));

        // When
        Executable executable = () -> githubService.getUserEmail(request, auth);

        // Then
        assertThrows(GithubEmailNotFoundException.class, executable);
    }

    @Test
    void getUserEmail_whenInEmptyArrayResponse_thenThrowGithubEmailNotFoundException() {
        // Given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        OAuth2AuthenticationToken auth = Mockito.mock(OAuth2AuthenticationToken.class);

        when(authorizedClientRepository.loadAuthorizedClient(any(), any(), any())).thenReturn(null);

        mockWebServer.enqueue(new MockResponse()
                .setBody("[]")
                .addHeader("Content-Type", "application/json"));

        // When
        Executable executable = () -> githubService.getUserEmail(request, auth);

        // Then
        assertThrows(GithubEmailNotFoundException.class, executable);
    }

}