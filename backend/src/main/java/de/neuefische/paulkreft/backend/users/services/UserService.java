package de.neuefische.paulkreft.backend.users.services;

import de.neuefische.paulkreft.backend.exception.GithubEmailNotFoundException;
import de.neuefische.paulkreft.backend.github.services.GithubService;
import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.services.TimeService;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.models.UserGet;
import de.neuefische.paulkreft.backend.users.repository.UsersRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepo usersRepo;
    private final IdService idService;
    private final TimeService timeService;
    private final GithubService githubService;

    public UserGet getLoggedInUser(Principal user, HttpServletRequest request) {
        if (user == null) {
            return null;
        }

        if (user instanceof UsernamePasswordAuthenticationToken) {
            return getEmailUser(user);
        }

        // If type is OAuth2AuthenticationToken
        return getOAuthUser(request);
    }

    private UserGet getEmailUser(Principal user) {
        String email = user.getName();

        if (!existsByEmail(email)) {
            return null;
        }

        User currentUser = usersRepo.findUserByEmail(email);

        return updateUser(currentUser.withLastActive(timeService.getNow()));
    }

    private UserGet getOAuthUser(HttpServletRequest request) {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        OAuth2AuthenticationToken auth =
                (OAuth2AuthenticationToken) authentication;

        String email = githubService.getUserEmail(request, auth);

        if (email == null) {
            throw new GithubEmailNotFoundException("Could not get GitHub email!");
        }

        boolean isReturningUser = usersRepo.existsUserByEmail(email);

        Instant now = timeService.getNow();
        if (!isReturningUser) {
            return new UserGet(usersRepo.save(new User(idService.generateUUID(), email, email, null, now, now)));
        }

        User currentUser = usersRepo.findUserByEmail(email);

        return updateUser(currentUser.withLastActive(now));
    }

    public UserGet createUser(String name, String email, String password) {
        Instant now = timeService.getNow();

        return new UserGet(usersRepo.save(new User(idService.generateUUID(), name, email, password, now, now)));
    }

    public UserGet updateUser(User user) {
        return new UserGet(usersRepo.save(user));
    }

    public boolean existsByEmail(String email) {
        return usersRepo.existsUserByEmail(email);
    }
}
