package com.harshith.talentbridge.dto;

import com.harshith.talentbridge.enums.JobStatus;
import com.harshith.talentbridge.enums.JobType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private JobType jobType;
    private JobStatus status;
    private Double salaryMin;
    private Double salaryMax;
    private Double minCgpa;
    private String experienceRequired;
    private List<String> requiredSkills;
    private Integer openings;
    private LocalDate applicationDeadline;

    // Student Eligibility calculation
    private Boolean isEligible;
    private String eligibilityReason;

    // Recruiter & Company Metadata
    private Long recruiterId;
    private String companyName;
    private String companyLogoUrl;
    private String companyLocation;
    private String companyWebsite;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}