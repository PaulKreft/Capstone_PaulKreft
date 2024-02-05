package de.neuefische.paulkreft.backend.security.services;

import de.neuefische.paulkreft.backend.security.models.SignUpRequest;
import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final UserService userService;

    public User signUpWithEmail(SignUpRequest request) {
        String email = request.email();
        String password = request.password();

        boolean isEmailValid = validateEmail(email);
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

    private boolean validateEmail(String email) {
        String regX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern emailPattern = Pattern.compile(regX);
        Matcher emailMatcher = emailPattern.matcher(email);

        return emailMatcher.matches();
    }

    private boolean validatePassword(String password) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[ !\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~\\\\])[A-Za-z\\d !\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~\\\\]{8,100}$");
        Matcher passwordMatcher = passwordPattern.matcher(password);

        return passwordMatcher.matches();
    }
}
