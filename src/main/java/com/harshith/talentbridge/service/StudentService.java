package com.harshith.talentbridge.service;

import com.harshith.talentbridge.dto.StudentProfileRequest;
import com.harshith.talentbridge.dto.StudentProfileResponse;
import com.harshith.talentbridge.entity.StudentProfile;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.repository.StudentRepository;
import com.harshith.talentbridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Create or Full Update Student Profile
    public StudentProfileResponse saveOrUpdateProfile(String email, StudentProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        StudentProfile profile = studentRepository.findByUser(user)
                .orElse(StudentProfile.builder().user(user).build());

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

        StudentProfile savedProfile = studentRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    // 2. Fetch Logged-in Student Profile
    public StudentProfileResponse getProfile(String email) {
        StudentProfile profile = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found for student with email: " + email));

        return mapToResponse(profile);
    }

    // 3. Dynamic Partial Update (Handles Any Single or Multiple Fields)
    public StudentProfileResponse patchProfile(String email, Map<String, Object> updates) {
        StudentProfile profile = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found for student with email: " + email));

        updates.forEach((key, value) -> {
            if (value != null) {
                switch (key) {
                    case "phone" -> profile.setPhone(value.toString());
                    case "university" -> profile.setUniversity(value.toString());
                    case "branch" -> profile.setBranch(value.toString());
                    case "domain" -> profile.setDomain(value.toString());
                    case "location" -> profile.setLocation(value.toString());
                    case "skills" -> profile.setSkills(value.toString());
                    case "cgpa" -> profile.setCgpa(Double.valueOf(value.toString()));
                    case "graduationYear" -> profile.setGraduationYear(Integer.valueOf(value.toString()));
                    case "bio" -> profile.setBio(value.toString());
                    case "linkedinUrl" -> profile.setLinkedinUrl(value.toString());
                    case "githubUrl" -> profile.setGithubUrl(value.toString());
                    case "portfolioUrl" -> profile.setPortfolioUrl(value.toString());
                    case "resumeUrl" -> profile.setResumeUrl(value.toString());
                }
            }
        });

        StudentProfile saved = studentRepository.save(profile);
        return mapToResponse(saved);
    }

    // 4. Update Resume URL
    public StudentProfileResponse updateResumeUrl(String email, String resumeUrl) {
        StudentProfile profile = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found for student with email: " + email));

        profile.setResumeUrl(resumeUrl);
        return mapToResponse(studentRepository.save(profile));
    }

    // 5. Update Skills Only
    public StudentProfileResponse updateSkills(String email, String skills) {
        StudentProfile profile = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found for student with email: " + email));

        profile.setSkills(skills);
        return mapToResponse(studentRepository.save(profile));
    }

    // 6. Update Contact Details (Phone, Location)
    public StudentProfileResponse updateContactInfo(String email, String phone, String location) {
        StudentProfile profile = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found for student with email: " + email));

        if (phone != null) profile.setPhone(phone);
        if (location != null) profile.setLocation(location);
        return mapToResponse(studentRepository.save(profile));
    }

    // 7. Update Academic Details
    public StudentProfileResponse updateAcademicInfo(String email, String university, String branch,
                                                     String domain, Double cgpa, Integer gradYear) {
        StudentProfile profile = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found for student with email: " + email));

        if (university != null) profile.setUniversity(university);
        if (branch != null) profile.setBranch(branch);
        if (domain != null) profile.setDomain(domain);
        if (cgpa != null) profile.setCgpa(cgpa);
        if (gradYear != null) profile.setGraduationYear(gradYear);
        return mapToResponse(studentRepository.save(profile));
    }

    // 8. Update Social / Portfolio Links
    public StudentProfileResponse updateSocialLinks(String email, String linkedinUrl,
                                                    String githubUrl, String portfolioUrl) {
        StudentProfile profile = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found for student with email: " + email));

        if (linkedinUrl != null) profile.setLinkedinUrl(linkedinUrl);
        if (githubUrl != null) profile.setGithubUrl(githubUrl);
        if (portfolioUrl != null) profile.setPortfolioUrl(portfolioUrl);
        return mapToResponse(studentRepository.save(profile));
    }

    // 9. Update Bio Only
    public StudentProfileResponse updateBio(String email, String bio) {
        StudentProfile profile = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found for student with email: " + email));

        profile.setBio(bio);
        return mapToResponse(studentRepository.save(profile));
    }

    // Helper Method: Entity -> Response DTO
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