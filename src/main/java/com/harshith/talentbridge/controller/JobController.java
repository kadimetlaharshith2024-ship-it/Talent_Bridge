package com.harshith.talentbridge.controller;

import com.harshith.talentbridge.dto.JobRequest;
import com.harshith.talentbridge.dto.JobResponse;
import com.harshith.talentbridge.enums.JobType;
import com.harshith.talentbridge.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> postJob(
            @Valid @RequestBody JobRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        JobResponse response = jobService.postJob(userDetails.getUsername(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<List<JobResponse>> getMyJobs(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<JobResponse> response = jobService.getMyPostedJobs(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<JobResponse>> getStudentJobFeed(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) JobType jobType,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<JobResponse> response = jobService.getJobsForStudent(userDetails.getUsername(), keyword, jobType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<JobResponse> getJobDetailsForStudent(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        JobResponse response = jobService.getJobByIdForStudent(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        JobResponse response = jobService.getJobById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        JobResponse response = jobService.updateJob(userDetails.getUsername(), id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<JobResponse> patchJob(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates,
            @AuthenticationPrincipal UserDetails userDetails) {
        JobResponse response = jobService.patchJob(userDetails.getUsername(), id, updates);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteJob(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        jobService.deleteJob(userDetails.getUsername(), id);
        return ResponseEntity.ok(Map.of("message", "Job deleted successfully"));
    }
}