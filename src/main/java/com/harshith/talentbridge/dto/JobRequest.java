package com.harshith.talentbridge.dto;

import com.harshith.talentbridge.enums.JobStatus;
import com.harshith.talentbridge.enums.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Job type is required")
    private JobType jobType;

    private JobStatus status;
    private Double salaryMin;
    private Double salaryMax;
    private Double minCgpa;
    private String experienceRequired;
    private List<String> requiredSkills;
    private Integer openings;
    private LocalDate applicationDeadline;
}