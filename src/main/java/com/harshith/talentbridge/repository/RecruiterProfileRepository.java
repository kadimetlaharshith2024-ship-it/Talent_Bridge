package com.harshith.talentbridge.repository;

import com.harshith.talentbridge.entity.RecruiterProfile;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Long> {
    Optional<RecruiterProfile> findByUser(User user);
    List<RecruiterProfile> findByVerificationStatus(VerificationStatus verificationStatus);
    long countByVerificationStatus(VerificationStatus verificationStatus);
}