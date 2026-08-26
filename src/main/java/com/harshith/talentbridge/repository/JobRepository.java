package com.harshith.talentbridge.repository;

import com.harshith.talentbridge.entity.Job;
import com.harshith.talentbridge.entity.RecruiterProfile;
import com.harshith.talentbridge.enums.JobStatus;
import com.harshith.talentbridge.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiter(RecruiterProfile recruiter);

    long countByStatus(JobStatus status);

    @Query("SELECT j FROM Job j WHERE " +
            "(:status IS NULL OR j.status = :status) AND " +
            "(:jobType IS NULL OR j.jobType = :jobType) AND " +
            "(:keyword IS NULL OR " +
            "LOWER(j.title) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR " +
            "LOWER(j.location) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))")
    List<Job> searchJobs(
            @Param("keyword") String keyword,
            @Param("jobType") JobType jobType,
            @Param("status") JobStatus status
    );
}