package de.neuefische.paulkreft.backend.github.services;

import de.neuefische.paulkreft.backend.exception.GithubEmailNotFoundException;
import de.neuefische.paulkreft.backend.github.models.GithubEmailResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class GithubService {
    private final OAuth2AuthorizedClientRepository authorizedClientRepo;
    private final RestClient restClient;

    public GithubService(OAuth2AuthorizedClientRepository authorizedClientRepo, @Value("https://api.github.com/user/emails") String url) {
        this.authorizedClientRepo = authorizedClientRepo;
        this.restClient = RestClient.builder().baseUrl(url).build();
    }

    public String getUserEmail(HttpServletRequest request, OAuth2AuthenticationToken auth) {
        final var authorizedClient = authorizedClientRepo.loadAuthorizedClient(auth.getAuthorizedClientRegistrationId(), auth, request);
        String token = Optional.ofNullable(authorizedClient).map(OAuth2AuthorizedClient::getAccessToken).map(OAuth2AccessToken::getTokenValue).orElse(null);

        GithubEmailResponse[] response = restClient.get()
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(GithubEmailResponse[].class);

        if (response == null || response.length == 0) {
            throw new GithubEmailNotFoundException("Could not retrieve GitHub email information!");
        }

        return response[0].email();
    }
}
