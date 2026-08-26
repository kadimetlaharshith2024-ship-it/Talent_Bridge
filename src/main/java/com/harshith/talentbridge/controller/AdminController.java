package com.harshith.talentbridge.controller;

import com.harshith.talentbridge.dto.*;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.enums.Role;
import com.harshith.talentbridge.enums.VerificationStatus;
import com.harshith.talentbridge.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    // --- Dashboard Stats ---
    @GetMapping("/dashboard/stats")
    public ResponseEntity<AdminDashboardStats> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // --- User Management & Granular Access ---
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean enabled) {
        return ResponseEntity.ok(adminService.getAllUsers(role, enabled));
    }

    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<User> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UserRoleUpdateRequest request) {
        return ResponseEntity.ok(adminService.updateUserRole(userId, request.getRole()));
    }

    @PatchMapping("/users/{userId}/access")
    public ResponseEntity<User> setUserAccess(
            @PathVariable Long userId,
            @RequestParam boolean enabled) {
        return ResponseEntity.ok(adminService.setUserAccess(userId, enabled));
    }

    @PostMapping("/users/bulk-access")
    public ResponseEntity<Map<String, String>> setBulkUserAccess(
            @Valid @RequestBody BulkUserStatusRequest request) {
        adminService.setBulkUserAccess(request.getUserIds(), request.getEnabled());
        return ResponseEntity.ok(Map.of("message", "Bulk access status updated successfully"));
    }

    // --- Recruiter Verification Management ---
    @GetMapping("/recruiters")
    public ResponseEntity<List<RecruiterProfileResponse>> getAllRecruiters(
            @RequestParam(required = false) VerificationStatus verificationStatus) {
        return ResponseEntity.ok(adminService.getAllRecruiters(verificationStatus));
    }

    @PatchMapping("/recruiters/{recruiterId}/verify")
    public ResponseEntity<RecruiterProfileResponse> verifyRecruiter(
            @PathVariable Long recruiterId,
            @Valid @RequestBody RecruiterVerificationRequest request) {
        return ResponseEntity.ok(adminService.updateRecruiterVerification(recruiterId, request.getStatus(), request.getNotes()));
    }

    // --- Job Moderation ---
    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(adminService.getAllJobsForAdmin());
    }

    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<Map<String, String>> closeJobByAdmin(@PathVariable Long jobId) {
        adminService.closeJobByAdmin(jobId);
        return ResponseEntity.ok(Map.of("message", "Job closed by administrator"));
    }
}