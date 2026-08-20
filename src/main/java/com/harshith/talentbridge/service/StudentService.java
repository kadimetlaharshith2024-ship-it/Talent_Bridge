package com.harshith.talentbridge.service;

import com.harshith.talentbridge.dto.StudentProfileRequest;
import com.harshith.talentbridge.dto.StudentProfileResponse;
import com.harshith.talentbridge.entity.StudentProfile;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.repository.StudentRepository;
import com.harshith.talentbridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Create or Update Student Profile
    public StudentProfileResponse saveOrUpdateProfile(String email, StudentProfileRequest request) {
        // Find user by email extracted from JWT
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // If profile exists -> fetch it to UPDATE; If not -> initialize a new profile to INSERT
        StudentProfile profile = studentRepository.findByUser(user)
                .orElse(StudentProfile.builder().user(user).build());

        // Update fields with incoming request data
        profile.setPhone(request.getPhone());
        profile.setUniversity(request.getUniversity());
        profile.setBranch(request.getBranch());
        profile.setDomain(request.getDomain());
        profile.setLocation(request.getLocation());
        profile.setSkills(request.getSkills());
        profile.setCgpa(request.getCgpa());
        profile.setGraduationYear(request.getGraduationYear());
        profile.setBio(request.getBio());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setResumeUrl(request.getResumeUrl());

        // Save to database
        StudentProfile savedProfile = studentRepository.save(profile);

        // Convert saved entity into response DTO
        return mapToResponse(savedProfile);
    }

    // 2. Fetch logged-in student profile
    public StudentProfileResponse getProfile(String email) {
        StudentProfile profile = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found for student with email: " + email));

        return mapToResponse(profile);
    }

    // 3. Upload / Update Resume URL Metadata
    public StudentProfileResponse updateResumeUrl(String email, String resumeUrl) {
        StudentProfile profile = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found for student with email: " + email));

        profile.setResumeUrl(resumeUrl);
        StudentProfile saved = studentRepository.save(profile);
        return mapToResponse(saved);
    }

    // Helper Method: Maps StudentProfile Entity + User Entity -> StudentProfileResponse DTO
    private StudentProfileResponse mapToResponse(StudentProfile profile) {
        return StudentProfileResponse.builder()
                .id(profile.getId())
                .name(profile.getUser().getName())
                .email(profile.getUser().getEmail())
                .role(profile.getUser().getRole().name())
                .phone(profile.getPhone())
                .university(profile.getUniversity())
                .branch(profile.getBranch())
                .domain(profile.getDomain())
                .location(profile.getLocation())
                .skills(profile.getSkills())
                .cgpa(profile.getCgpa())
                .graduationYear(profile.getGraduationYear())
                .bio(profile.getBio())
                .linkedinUrl(profile.getLinkedinUrl())
                .githubUrl(profile.getGithubUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .resumeUrl(profile.getResumeUrl())
                .build();
    }
}