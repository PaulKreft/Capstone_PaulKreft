package de.neuefische.paulkreft.backend.user.controller;

import de.neuefische.paulkreft.backend.statistic.model.ClassicStatistics;
import de.neuefische.paulkreft.backend.user.model.User;
import de.neuefische.paulkreft.backend.user.model.UserGet;
import de.neuefische.paulkreft.backend.user.service.UserService;
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
    public UserGet getUser(Principal principal, HttpServletRequest request) {
        return userService.getLoggedInUser(principal, request);
    }

    @PutMapping
    public UserGet updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("{id}/statistics")
    public ClassicStatistics getUserStatistics(@PathVariable String id) {
        return userService.getStatistics(id);
    }
}
