package com.harshith.talentbridge.repository;

import com.harshith.talentbridge.entity.Application;
import com.harshith.talentbridge.entity.Job;
import com.harshith.talentbridge.entity.StudentProfile;
import com.harshith.talentbridge.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByStudentAndJob(StudentProfile student, Job job);

    boolean existsByStudentAndJob(StudentProfile student, Job job);

    List<Application> findByStudentOrderByAppliedAtDesc(StudentProfile student);

    List<Application> findByJobOrderByAppliedAtDesc(Job job);

    List<Application> findByJobAndStatusOrderByAppliedAtDesc(Job job, ApplicationStatus status);

    @Modifying
    @Transactional
    @Query("DELETE FROM Application a WHERE a.job = :job")
    void deleteByJob(@Param("job") Job job);
}