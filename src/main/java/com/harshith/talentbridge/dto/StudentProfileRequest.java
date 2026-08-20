package com.harshith.talentbridge.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileRequest {

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotBlank(message = "University name is required")
    @Size(min = 2, max = 150, message = "University name must be between 2 and 150 characters")
    private String university;

    @NotBlank(message = "Branch is required")
    @Size(min = 2, max = 100, message = "Branch must be between 2 and 100 characters")
    private String branch;

    @Size(max = 100, message = "Domain must not exceed 100 characters")
    private String domain;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    @Size(max = 500, message = "Skills text must not exceed 500 characters")
    private String skills;

    @NotNull(message = "CGPA is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "CGPA cannot be negative")
    @DecimalMax(value = "10.0", inclusive = true, message = "CGPA cannot exceed 10.0")
    private Double cgpa;

    @Min(value = 2000, message = "Graduation year must be valid")
    @Max(value = 2035, message = "Graduation year cannot be beyond 2035")
    private Integer graduationYear;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String resumeUrl;
}