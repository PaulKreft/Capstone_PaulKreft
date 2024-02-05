package de.neuefische.paulkreft.backend.users.controller;

import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public User getUser(Principal principal, HttpServletRequest request) {
        return userService.getUser(principal, request);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
}
