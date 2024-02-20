package de.neuefische.paulkreft.backend.user.repository;

import de.neuefische.paulkreft.backend.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends MongoRepository<User, String> {
    User findUserByEmail(String email);

    Boolean existsUserByEmail(String email);
}
