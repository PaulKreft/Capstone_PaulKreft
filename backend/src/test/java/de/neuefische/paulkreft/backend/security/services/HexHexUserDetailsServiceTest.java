package de.neuefische.paulkreft.backend.security.services;

import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.repository.UsersRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class HexHexUserDetailsServiceTest {

    @Test
    void loadUserByUsernameTest_whenUserFound_returnUserDetails() {
        // Given
        UsersRepo usersRepo = Mockito.mock(UsersRepo.class);
        String email = "testemail@as.de";
        String password = "myPassword";

        User user = new User("1", "", email, password, Instant.now(), Instant.now());
        when(usersRepo.findUserByEmail(email)).thenReturn(user);

        HexHexUserDetailsService userDetailsService = new HexHexUserDetailsService(usersRepo);

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

        HexHexUserDetailsService userDetailsService = new HexHexUserDetailsService(usersRepo);

        // When
        Executable executable = () -> userDetailsService.loadUserByUsername(email);

        // Then
        assertThrows(UsernameNotFoundException.class, executable);
    }
}