package de.neuefische.paulkreft.backend.security.services;

import de.neuefische.paulkreft.backend.security.models.SignUpRequest;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SignUpService {

    public String signUpWithEmail(SignUpRequest request) {
        boolean isEmailValid = validateEmail(request.email());
        boolean isPasswordValid = validatePassword(request.password());

        if (!(isEmailValid && isPasswordValid)) {
            return null;
        }

        return request.email();
    }

    private boolean validateEmail(String email) {
        Pattern emailPattern = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        Matcher emailMatcher = emailPattern.matcher(email);

        return emailMatcher.matches();
    }

    private boolean validatePassword(String password) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[ !\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~\\\\])[A-Za-z\\d !\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~\\\\]{8,100}$");
        Matcher passwordMatcher = passwordPattern.matcher(password);

        return passwordMatcher.matches();
    }
}
