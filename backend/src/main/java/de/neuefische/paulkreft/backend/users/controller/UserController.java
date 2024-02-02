package de.neuefische.paulkreft.backend.users.controller;

import de.neuefische.paulkreft.backend.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import de.neuefische.paulkreft.backend.users.models.User;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public User getLoggedInUser(@AuthenticationPrincipal OAuth2User principal) {
        return userService.getUser(principal);
    }
}
