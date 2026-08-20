package com.harshith.talentbridge.repository;

import com.harshith.talentbridge.entity.StudentProfile;
import com.harshith.talentbridge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentProfile, Long> {

    // Find profile by passing the User object
    Optional<StudentProfile> findByUser(User user);

    // Find profile by the user's email directly
    Optional<StudentProfile> findByUserEmail(String email);
}