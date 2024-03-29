package de.neuefische.paulkreft.backend.security.service;

import de.neuefische.paulkreft.backend.user.model.User;
import de.neuefische.paulkreft.backend.user.repository.UsersRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    @Test
    void loadUserByUsernameTest_whenUserFound_returnUserDetails() {
        // Given
        UsersRepo usersRepo = Mockito.mock(UsersRepo.class);
        String email = "testemail@as.de";
        String password = "myPassword";

        User user = new User("1", "", email, password, Instant.now(), Instant.now());
        when(usersRepo.findUserByEmail(email)).thenReturn(user);

        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(usersRepo);

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameTest_whenUserNotFound_throwUsernameNotFoundException() {
        // Given
        UsersRepo usersRepo = Mockito.mock(UsersRepo.class);
        String email = "testemail@as.de";

        when(usersRepo.findUserByEmail("testemail@as.de")).thenReturn(null);

        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(usersRepo);

        // When
        Executable executable = () -> userDetailsService.loadUserByUsername(email);

        // Then
        assertThrows(UsernameNotFoundException.class, executable);
    }
}