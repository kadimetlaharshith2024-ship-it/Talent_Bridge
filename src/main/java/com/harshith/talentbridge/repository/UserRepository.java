package com.harshith.talentbridge.repository;//tells java that this class belongs to repository package

import com.harshith.talentbridge.entity.User;//this repository is going to work with the User entity
import org.springframework.data.jpa.repository.JpaRepository; //Instead of writing SQL yourself Spring generates the implementation

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User>findByEmail(String email);
    boolean existsByEmail(String email);

}
