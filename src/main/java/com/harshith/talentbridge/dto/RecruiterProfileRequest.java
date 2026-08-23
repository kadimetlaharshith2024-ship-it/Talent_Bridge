package com.harshith.talentbridge.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfileRequest {
    // Recruiter Info
    private String designation;
    private String contactPhone;
    private String alternateEmail;

    // Company Info
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
}