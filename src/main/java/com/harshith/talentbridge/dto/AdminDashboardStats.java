package com.harshith.talentbridge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardStats {
    private long totalUsers;
    private long totalStudents;
    private long totalRecruiters;
    private long pendingRecruiterVerifications;
    private long totalJobs;
    private long activeJobs;
    private long totalApplications;
    private long totalResumes;
    private Map<String, Long> applicationsByStatus;
}