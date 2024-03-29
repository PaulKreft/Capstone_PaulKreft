package de.neuefische.paulkreft.backend.security.service;

import de.neuefische.paulkreft.backend.exception.EmailAlreadyRegisteredException;
import de.neuefische.paulkreft.backend.security.model.SignUpRequest;
import de.neuefische.paulkreft.backend.user.model.UserGet;
import de.neuefische.paulkreft.backend.user.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final UserService userService;

    public UserGet signUpWithEmail(SignUpRequest request) throws EmailAlreadyRegisteredException {
        String email = request.email();
        String password = request.password();

        boolean isEmailValid = validateEmail(request);
        boolean isPasswordValid = validatePassword(password);

        if (!(isEmailValid && isPasswordValid)) {
            throw new IllegalArgumentException("Email or password invalid!");
        }

        if (userService.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException("Email already taken!");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String hashedPassword = encoder.encode(password);

        return userService.createUser(email, email, hashedPassword);
    }

    private boolean validateEmail(SignUpRequest request) {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Set<ConstraintViolation<SignUpRequest>> violations = validatorFactory.getValidator().validate(request);
            if (!violations.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private boolean validatePassword(String password) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[ !@#$%^&*])[A-Za-z\\d !@#$%^&*]{8,100}$");
        Matcher passwordMatcher = passwordPattern.matcher(password);

        return passwordMatcher.matches();
    }
}
