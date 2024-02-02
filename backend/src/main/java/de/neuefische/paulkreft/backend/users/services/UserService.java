package de.neuefische.paulkreft.backend.users.services;

import de.neuefische.paulkreft.backend.services.IdService;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.repositories.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepo usersRepo;
    private final IdService idService;

    public User getUser(OAuth2User user) {
        if (user == null) {
            return null;
        }

        Integer githubId = user.getAttribute("id");

        if (githubId == null) {
            return null;
        }

        String name = user.getAttribute("login");
        boolean isReturningUser = usersRepo.existsUserByGithubId(githubId);

        if (!isReturningUser) {
            return usersRepo.save(new User(idService.generateUUID(), githubId, name));
        }

        return usersRepo.findUserByGithubId(githubId);
    }
}
