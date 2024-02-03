package de.neuefische.paulkreft.backend.users.services;

import de.neuefische.paulkreft.backend.github.services.GithubService;
import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.services.TimeService;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.repositories.UsersRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepo usersRepo;
    private final IdService idService;
    private final TimeService timeService;
    private final GithubService githubService;

    public User getUser(OAuth2User user, HttpServletRequest request, OAuth2AuthenticationToken auth) {
        if (user == null) {
            return null;
        }

        String name = user.getAttribute("login");

        String email = githubService.getUserEmail(request, auth);

        if (email == null) {
            throw new RuntimeException();
        }

        boolean isReturningUser = usersRepo.existsUserByEmail(email);

        Instant now = timeService.getNow();
        if (!isReturningUser) {
            return usersRepo.save(new User(idService.generateUUID(), name, email, now, now));
        }

        User currentUser = usersRepo.findUserByEmail(email);

        return updateUser(currentUser.withLastActive(now));
    }

    public User updateUser(User user) {
        return usersRepo.save(user);
    }
}
