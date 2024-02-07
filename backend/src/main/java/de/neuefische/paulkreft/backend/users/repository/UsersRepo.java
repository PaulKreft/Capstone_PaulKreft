package de.neuefische.paulkreft.backend.users.repository;

import de.neuefische.paulkreft.backend.users.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends MongoRepository<User, String> {
    User findUserByEmail(String email);

    Boolean existsUserByEmail(String email);
}
