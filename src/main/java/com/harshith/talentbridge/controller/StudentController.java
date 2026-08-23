package com.harshith.talentbridge.controller;

import com.harshith.talentbridge.dto.StudentProfileRequest;
import com.harshith.talentbridge.dto.StudentProfileResponse;
import com.harshith.talentbridge.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student/profile")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 1. Create or Replace Full Profile
    @PostMapping
    public ResponseEntity<StudentProfileResponse> createOrUpdateProfile(
            @Valid @RequestBody StudentProfileRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        StudentProfileResponse response = studentService.saveOrUpdateProfile(userDetails.getUsername(), request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 2. View Logged-in Student Profile
    @GetMapping("/me")
    public ResponseEntity<StudentProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        StudentProfileResponse response = studentService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    // 3. Dynamic Partial Update (Accepts any combination of fields in JSON)
    @PatchMapping("/me")
    public ResponseEntity<StudentProfileResponse> patchProfile(
            @RequestBody Map<String, Object> updates,
            @AuthenticationPrincipal UserDetails userDetails) {
        StudentProfileResponse response = studentService.patchProfile(userDetails.getUsername(), updates);
        return ResponseEntity.ok(response);
    }

    // 4. Update Resume URL
    @PatchMapping("/me/resume")
    public ResponseEntity<StudentProfileResponse> updateResumeUrl(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        String resumeUrl = payload.get("resumeUrl");
        StudentProfileResponse response = studentService.updateResumeUrl(userDetails.getUsername(), resumeUrl);
        return ResponseEntity.ok(response);
    }

    // 5. Update Skills Only
    @PatchMapping("/me/skills")
    public ResponseEntity<StudentProfileResponse> updateSkills(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        String skills = payload.get("skills");
        StudentProfileResponse response = studentService.updateSkills(userDetails.getUsername(), skills);
        return ResponseEntity.ok(response);
    }

    // 6. Update Contact Details (phone, location)
    @PatchMapping("/me/contact")
    public ResponseEntity<StudentProfileResponse> updateContact(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        StudentProfileResponse response = studentService.updateContactInfo(
                userDetails.getUsername(),
                payload.get("phone"),
                payload.get("location")
        );
        return ResponseEntity.ok(response);
    }

    // 7. Update Academic Details
    @PatchMapping("/me/academics")
    public ResponseEntity<StudentProfileResponse> updateAcademics(
            @RequestBody Map<String, Object> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        String university = (String) payload.get("university");
        String branch = (String) payload.get("branch");
        String domain = (String) payload.get("domain");
        Double cgpa = payload.get("cgpa") != null ? Double.valueOf(payload.get("cgpa").toString()) : null;
        Integer gradYear = payload.get("graduationYear") != null ? Integer.valueOf(payload.get("graduationYear").toString()) : null;

        StudentProfileResponse response = studentService.updateAcademicInfo(
                userDetails.getUsername(), university, branch, domain, cgpa, gradYear
        );
        return ResponseEntity.ok(response);
    }

    // 8. Update Social Links
    @PatchMapping("/me/links")
    public ResponseEntity<StudentProfileResponse> updateSocialLinks(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        StudentProfileResponse response = studentService.updateSocialLinks(
                userDetails.getUsername(),
                payload.get("linkedinUrl"),
                payload.get("githubUrl"),
                payload.get("portfolioUrl")
        );
        return ResponseEntity.ok(response);
    }

    // 9. Update Bio Only
    @PatchMapping("/me/bio")
    public ResponseEntity<StudentProfileResponse> updateBio(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        String bio = payload.get("bio");
        StudentProfileResponse response = studentService.updateBio(userDetails.getUsername(), bio);
        return ResponseEntity.ok(response);
    }
}