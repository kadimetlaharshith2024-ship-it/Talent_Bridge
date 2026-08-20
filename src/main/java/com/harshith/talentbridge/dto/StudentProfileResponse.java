package com.harshith.talentbridge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String phone;
    private String university;
    private String branch;
    private String domain;
    private String location;
    private String skills;
    private Double cgpa;
    private Integer graduationYear;
    private String bio;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String resumeUrl;
}