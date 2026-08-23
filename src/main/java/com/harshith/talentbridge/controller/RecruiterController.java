package com.harshith.talentbridge.controller;

import com.harshith.talentbridge.dto.RecruiterProfileRequest;
import com.harshith.talentbridge.dto.RecruiterProfileResponse;
import com.harshith.talentbridge.service.RecruiterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/recruiter/profile")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    // 1. Create or Overwrite Full Profile
    @PostMapping
    public ResponseEntity<RecruiterProfileResponse> createOrUpdateProfile(
            @Valid @RequestBody RecruiterProfileRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        RecruiterProfileResponse response = recruiterService.saveOrUpdateProfile(userDetails.getUsername(), request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 2. View Logged-in Recruiter Profile
    @GetMapping("/me")
    public ResponseEntity<RecruiterProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        RecruiterProfileResponse response = recruiterService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    // 3. Dynamic Partial Update (Any combination of fields)
    @PatchMapping("/me")
    public ResponseEntity<RecruiterProfileResponse> patchProfile(
            @RequestBody Map<String, Object> updates,
            @AuthenticationPrincipal UserDetails userDetails) {
        RecruiterProfileResponse response = recruiterService.patchProfile(userDetails.getUsername(), updates);
        return ResponseEntity.ok(response);
    }

    // 4. Update Company Details
    @PatchMapping("/me/company")
    public ResponseEntity<RecruiterProfileResponse> updateCompanyDetails(
            @RequestBody Map<String, Object> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        String name = (String) payload.get("companyName");
        String website = (String) payload.get("companyWebsite");
        String location = (String) payload.get("companyLocation");
        String industry = (String) payload.get("industry");
        String size = (String) payload.get("companySize");
        Integer year = payload.get("establishedYear") != null ? Integer.valueOf(payload.get("establishedYear").toString()) : null;

        RecruiterProfileResponse response = recruiterService.updateCompanyDetails(
                userDetails.getUsername(), name, website, location, industry, size, year
        );
        return ResponseEntity.ok(response);
    }

    // 5. Update Contact Info (Phone, Alternate Email, HQ Address)
    @PatchMapping("/me/contact")
    public ResponseEntity<RecruiterProfileResponse> updateContact(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        RecruiterProfileResponse response = recruiterService.updateContactInfo(
                userDetails.getUsername(),
                payload.get("contactPhone"),
                payload.get("alternateEmail"),
                payload.get("headquartersAddress")
        );
        return ResponseEntity.ok(response);
    }

    // 6. Update Media & Social Links (Logo, Banner, LinkedIn, Twitter)
    @PatchMapping("/me/media")
    public ResponseEntity<RecruiterProfileResponse> updateMediaAndSocials(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        RecruiterProfileResponse response = recruiterService.updateMediaAndSocials(
                userDetails.getUsername(),
                payload.get("companyLogoUrl"),
                payload.get("companyCoverImageUrl"),
                payload.get("linkedinUrl"),
                payload.get("twitterUrl")
        );
        return ResponseEntity.ok(response);
    }

    // 7. Update Company Description / About Only
    @PatchMapping("/me/about")
    public ResponseEntity<RecruiterProfileResponse> updateDescription(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails) {
        String description = payload.get("companyDescription");
        RecruiterProfileResponse response = recruiterService.updateDescription(userDetails.getUsername(), description);
        return ResponseEntity.ok(response);
    }
}