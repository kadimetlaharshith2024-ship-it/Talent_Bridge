package com.harshith.talentbridge.dto;

import com.harshith.talentbridge.enums.ApplicationStatus;
import com.harshith.talentbridge.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;

    // Student Details
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private String studentBranch;
    private String studentUniversity;
    private Double studentCgpa;
    private String studentSkills;

    // Job Details
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String jobLocation;
    private JobType jobType;
    private Double minCgpaRequired;

    // Application Specifics
    private ApplicationStatus status;
    private String coverLetter;
    private String resumeUrl;
    private String recruiterFeedback;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}