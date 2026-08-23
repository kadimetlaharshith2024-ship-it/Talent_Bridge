package com.harshith.talentbridge.dto;

import com.harshith.talentbridge.enums.VerificationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfileResponse {

    // Identity & User Mapping
    private Long id;
    private Long userId;
    private String userEmail;

    // Recruiter Personal Info
    private String designation;
    private String contactPhone;
    private String alternateEmail;

    // Company Corporate Info
    private String companyName;
    private String companyWebsite;
    private String companyLocation;
    private String headquartersAddress;
    private String industry;
    private String companySize;
    private Integer establishedYear;
    private String companyDescription;

    // Media & Socials
    private String companyLogoUrl;
    private String companyCoverImageUrl;
    private String linkedinUrl;
    private String twitterUrl;

    // Verification & Notes
    private VerificationStatus verificationStatus;
    private String verificationNotes;

    // Audit Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}