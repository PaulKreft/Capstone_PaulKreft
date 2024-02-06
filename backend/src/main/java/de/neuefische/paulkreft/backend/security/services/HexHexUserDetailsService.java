package de.neuefische.paulkreft.backend.security.services;

import de.neuefische.paulkreft.backend.users.models.User;
import de.neuefische.paulkreft.backend.users.repositories.UsersRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HexHexUserDetailsService implements UserDetailsService {
    private final UsersRepo usersRepo;

    public HexHexUserDetailsService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usersRepo.findUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(user.email(), user.password(), List.of());
    }
}