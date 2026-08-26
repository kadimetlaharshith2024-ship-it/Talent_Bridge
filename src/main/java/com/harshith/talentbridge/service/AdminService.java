package com.harshith.talentbridge.service;

import com.harshith.talentbridge.dto.AdminDashboardStats;
import com.harshith.talentbridge.dto.JobResponse;
import com.harshith.talentbridge.dto.RecruiterProfileResponse;
import com.harshith.talentbridge.entity.Job;
import com.harshith.talentbridge.entity.RecruiterProfile;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.enums.JobStatus;
import com.harshith.talentbridge.enums.Role;
import com.harshith.talentbridge.enums.VerificationStatus;
import com.harshith.talentbridge.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final ResumeRepository resumeRepository;
    private final JobService jobService;
    private final RecruiterService recruiterService;

    // --- Dashboard Metrics ---
    @Transactional(readOnly = true)
    public AdminDashboardStats getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalStudents = studentRepository.count();
        long totalRecruiters = recruiterRepository.count();
        long pendingVerifications = recruiterRepository.countByVerificationStatus(VerificationStatus.PENDING);
        long totalJobs = jobRepository.count();
        long activeJobs = jobRepository.countByStatus(JobStatus.ACTIVE);
        long totalApplications = applicationRepository.count();
        long totalResumes = resumeRepository.count();

        Map<String, Long> applicationsByStatus = applicationRepository.findAll().stream()
                .collect(Collectors.groupingBy(app -> app.getStatus().name(), Collectors.counting()));

        return AdminDashboardStats.builder()
                .totalUsers(totalUsers)
                .totalStudents(totalStudents)
                .totalRecruiters(totalRecruiters)
                .pendingRecruiterVerifications(pendingVerifications)
                .totalJobs(totalJobs)
                .activeJobs(activeJobs)
                .totalApplications(totalApplications)
                .totalResumes(totalResumes)
                .applicationsByStatus(applicationsByStatus)
                .build();
    }

    // --- Dynamic Role & User Access Management ---
    @Transactional(readOnly = true)
    public List<User> getAllUsers(Role roleFilter, Boolean enabledFilter) {
        return userRepository.findAll().stream()
                .filter(u -> roleFilter == null || u.getRole() == roleFilter)
                .filter(u -> enabledFilter == null || u.isEnabled() == enabledFilter)
                .toList();
    }

    @Transactional
    public User updateUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setRole(newRole);
        return userRepository.save(user);
    }

    @Transactional
    public User setUserAccess(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    @Transactional
    public void setBulkUserAccess(List<Long> userIds, boolean enabled) {
        List<User> users = userRepository.findAllById(userIds);
        users.forEach(u -> u.setEnabled(enabled));
        userRepository.saveAll(users);
    }

    // --- Recruiter Verification ---
    @Transactional(readOnly = true)
    public List<RecruiterProfileResponse> getAllRecruiters(VerificationStatus verificationStatus) {
        List<RecruiterProfile> profiles = (verificationStatus != null)
                ? recruiterRepository.findByVerificationStatus(verificationStatus)
                : recruiterRepository.findAll();

        return profiles.stream()
                .map(recruiterService::mapToResponse)
                .toList();
    }

    @Transactional
    public RecruiterProfileResponse updateRecruiterVerification(Long recruiterId, VerificationStatus status, String notes) {
        RecruiterProfile profile = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found with ID: " + recruiterId));

        profile.setVerificationStatus(status);
        profile.setVerificationNotes(notes);
        RecruiterProfile saved = recruiterRepository.save(profile);

        return recruiterService.mapToResponse(saved);
    }

    // --- Job Moderation ---
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobsForAdmin() {
        return jobRepository.findAll().stream()
                .map(jobService::mapToResponse)
                .toList();
    }

    @Transactional
    public void closeJobByAdmin(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
        job.setStatus(JobStatus.CLOSED);
        jobRepository.save(job);
    }
}