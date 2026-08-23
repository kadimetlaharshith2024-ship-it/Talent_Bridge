package com.harshith.talentbridge.repository;

import com.harshith.talentbridge.entity.RecruiterProfile;
import com.harshith.talentbridge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<RecruiterProfile, Long> {

    // Find profile using the User entity
    Optional<RecruiterProfile> findByUser(User user);

    // Find profile directly by User ID
    Optional<RecruiterProfile> findByUserId(Long userId);

    // Check if a profile already exists for a user
    boolean existsByUserId(Long userId);
}