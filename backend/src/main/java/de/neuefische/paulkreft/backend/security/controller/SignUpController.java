package de.neuefische.paulkreft.backend.security.controller;

import de.neuefische.paulkreft.backend.security.models.SignUpRequest;
import de.neuefische.paulkreft.backend.security.services.SignUpService;
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
    public String signUpWithEmail(@RequestBody SignUpRequest request) {
        return signUpService.signUpWithEmail(request);
    }
}