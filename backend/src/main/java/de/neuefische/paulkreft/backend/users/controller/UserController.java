package de.neuefische.paulkreft.backend.users.controller;

import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public User getLoggedInUser(@AuthenticationPrincipal OAuth2User principal, HttpServletRequest request, OAuth2AuthenticationToken auth) {
        return userService.getUser(principal, request, auth);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
}
