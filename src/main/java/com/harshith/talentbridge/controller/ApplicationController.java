package com.harshith.talentbridge.controller;

import com.harshith.talentbridge.dto.ApplicationRequest;
import com.harshith.talentbridge.dto.ApplicationResponse;
import com.harshith.talentbridge.dto.ApplicationStatusUpdateRequest;
import com.harshith.talentbridge.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // Student: Apply for a job
    @PostMapping("/apply/{jobId}")
    public ResponseEntity<ApplicationResponse> apply(
            @PathVariable Long jobId,
            @RequestBody(required = false) ApplicationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ApplicationResponse response = applicationService.applyForJob(userDetails.getUsername(), jobId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Student: Withdraw application
    @PutMapping("/withdraw/{applicationId}")
    public ResponseEntity<ApplicationResponse> withdraw(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal UserDetails userDetails) {
        ApplicationResponse response = applicationService.withdrawApplication(userDetails.getUsername(), applicationId);
        return ResponseEntity.ok(response);
    }

    // Student: Track personal applications
    @GetMapping("/my-applications")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<ApplicationResponse> response = applicationService.getMyApplications(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    // Recruiter: View all applicants for a job
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicantsForJob(
            @PathVariable Long jobId,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<ApplicationResponse> response = applicationService.getApplicantsForJob(userDetails.getUsername(), jobId);
        return ResponseEntity.ok(response);
    }

    // Recruiter: Update application status (Shortlist / Reject / Interview)
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ApplicationResponse response = applicationService.updateApplicationStatus(userDetails.getUsername(), applicationId, request);
        return ResponseEntity.ok(response);
    }
}