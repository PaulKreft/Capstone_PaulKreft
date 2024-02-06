package de.neuefische.paulkreft.backend.security.services;

import de.neuefische.paulkreft.backend.security.models.SignUpRequest;
import de.neuefische.paulkreft.backend.users.models.UserGet;
import de.neuefische.paulkreft.backend.users.services.UserService;
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

    public UserGet signUpWithEmail(SignUpRequest request) {
        String email = request.email();
        String password = request.password();

        boolean isEmailValid = validateEmail(request);
        boolean isPasswordValid = validatePassword(password);

        if (!(isEmailValid && isPasswordValid)) {
            return null;
        }

        if (userService.existsByEmail(email)) {
            return null;
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
