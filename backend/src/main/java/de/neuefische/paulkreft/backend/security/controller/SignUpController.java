package de.neuefische.paulkreft.backend.security.controller;

import de.neuefische.paulkreft.backend.exception.EmailAlreadyRegisteredException;
import de.neuefische.paulkreft.backend.security.model.SignUpRequest;
import de.neuefische.paulkreft.backend.security.service.SignUpService;
import de.neuefische.paulkreft.backend.user.model.UserGet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signup")
public class SignUpController {
    SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("/email")
    public UserGet signUpWithEmail(@RequestBody SignUpRequest request) throws EmailAlreadyRegisteredException, IllegalArgumentException {
        return signUpService.signUpWithEmail(request);
    }
}