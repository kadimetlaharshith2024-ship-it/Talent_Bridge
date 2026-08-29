package com.harshith.talentbridge.service;

import com.harshith.talentbridge.dto.JobRequest;
import com.harshith.talentbridge.dto.JobResponse;
import com.harshith.talentbridge.entity.Job;
import com.harshith.talentbridge.entity.RecruiterProfile;
import com.harshith.talentbridge.entity.StudentProfile;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.enums.JobStatus;
import com.harshith.talentbridge.enums.JobType;
import com.harshith.talentbridge.repository.ApplicationRepository;
import com.harshith.talentbridge.repository.JobRepository;
import com.harshith.talentbridge.repository.RecruiterRepository;
import com.harshith.talentbridge.repository.StudentRepository;
import com.harshith.talentbridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public JobResponse postJob(String email, JobRequest request) {
        RecruiterProfile recruiter = getRecruiterByEmail(email);

        Job job = Job.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription().trim())
                .location(request.getLocation().trim())
                .jobType(request.getJobType())
                .status(request.getStatus() != null ? request.getStatus() : JobStatus.OPEN)
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .minCgpa(request.getMinCgpa())
                .experienceRequired(trimIfNotNull(request.getExperienceRequired()))
                .requiredSkills(request.getRequiredSkills() != null ? request.getRequiredSkills() : new ArrayList<>())
                .openings(request.getOpenings() != null && request.getOpenings() > 0 ? request.getOpenings() : 1)
                .applicationDeadline(request.getApplicationDeadline())
                .recruiter(recruiter)
                .build();

        return mapToResponse(jobRepository.save(job));
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getMyPostedJobs(String email) {
        RecruiterProfile recruiter = getRecruiterByEmail(email);
        return jobRepository.findByRecruiter(recruiter)
                .stream()
                .filter(job -> job.getStatus() != JobStatus.CLOSED)
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getJobsForStudent(String studentEmail, String keyword, JobType jobType) {
        StudentProfile studentProfile = studentRepository.findByUserEmail(studentEmail).orElse(null);
        Double studentCgpa = (studentProfile != null) ? studentProfile.getCgpa() : null;

        String queryKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;

        return jobRepository.searchJobs(queryKeyword, jobType, JobStatus.OPEN)
                .stream()
                .map(job -> mapToResponseWithEligibility(job, studentCgpa))
                .toList();
    }

    @Transactional(readOnly = true)
    public JobResponse getJobByIdForStudent(Long jobId, String studentEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        StudentProfile studentProfile = studentRepository.findByUserEmail(studentEmail).orElse(null);
        Double studentCgpa = (studentProfile != null) ? studentProfile.getCgpa() : null;

        return mapToResponseWithEligibility(job, studentCgpa);
    }

    @Transactional(readOnly = true)
    public JobResponse getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
        return mapToResponse(job);
    }

    @Transactional
    public JobResponse updateJob(String email, Long jobId, JobRequest request) {
        Job job = getJobAndValidateOwnership(email, jobId);

        job.setTitle(request.getTitle().trim());
        job.setDescription(request.getDescription().trim());
        job.setLocation(request.getLocation().trim());
        job.setJobType(request.getJobType());
        if (request.getStatus() != null) job.setStatus(request.getStatus());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setMinCgpa(request.getMinCgpa());
        job.setExperienceRequired(trimIfNotNull(request.getExperienceRequired()));
        if (request.getRequiredSkills() != null) job.setRequiredSkills(request.getRequiredSkills());
        if (request.getOpenings() != null && request.getOpenings() > 0) job.setOpenings(request.getOpenings());
        job.setApplicationDeadline(request.getApplicationDeadline());

        return mapToResponse(jobRepository.save(job));
    }

    @Transactional
    public JobResponse patchJob(String email, Long jobId, Map<String, Object> updates) {
        Job job = getJobAndValidateOwnership(email, jobId);

        updates.forEach((key, value) -> {
            if (value != null) {
                String strVal = value.toString().trim();
                switch (key) {
                    case "title" -> job.setTitle(strVal);
                    case "description" -> job.setDescription(strVal);
                    case "location" -> job.setLocation(strVal);
                    case "experienceRequired" -> job.setExperienceRequired(strVal);
                    case "jobType" -> {
                        try { job.setJobType(JobType.valueOf(strVal.toUpperCase())); } catch (Exception ignored) {}
                    }
                    case "status" -> {
                        try { job.setStatus(JobStatus.valueOf(strVal.toUpperCase())); } catch (Exception ignored) {}
                    }
                    case "salaryMin" -> {
                        try { job.setSalaryMin(Double.valueOf(strVal)); } catch (Exception ignored) {}
                    }
                    case "salaryMax" -> {
                        try { job.setSalaryMax(Double.valueOf(strVal)); } catch (Exception ignored) {}
                    }
                    case "minCgpa" -> {
                        try { job.setMinCgpa(Double.valueOf(strVal)); } catch (Exception ignored) {}
                    }
                    case "openings" -> {
                        try { job.setOpenings(Integer.valueOf(strVal)); } catch (Exception ignored) {}
                    }
                    case "applicationDeadline" -> {
                        try { job.setApplicationDeadline(LocalDate.parse(strVal)); } catch (Exception ignored) {}
                    }
                    case "requiredSkills" -> {
                        if (value instanceof List<?> list) {
                            job.setRequiredSkills(list.stream().map(Object::toString).toList());
                        }
                    }
                }
            }
        });

        return mapToResponse(jobRepository.save(job));
    }

    @Transactional
    public JobResponse deleteJob(String email, Long jobId) {
        Job job = getJobAndValidateOwnership(email, jobId);

        // 1. Delete associated applications to avoid foreign key errors
        applicationRepository.deleteByJob(job);

        // 2. Permanently remove the job entity
        jobRepository.delete(job);

        return mapToResponse(job);
    }

    private RecruiterProfile getRecruiterByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return recruiterRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found. Please complete your recruiter profile first."));
    }

    private Job getJobAndValidateOwnership(String email, Long jobId) {
        RecruiterProfile recruiter = getRecruiterByEmail(email);
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("Access denied: You do not own this job posting.");
        }
        return job;
    }

    private String trimIfNotNull(String str) {
        return (str != null) ? str.trim() : null;
    }

    public JobResponse mapToResponse(Job job) {
        RecruiterProfile recruiter = job.getRecruiter();
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .status(job.getStatus())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .minCgpa(job.getMinCgpa())
                .experienceRequired(job.getExperienceRequired())
                .requiredSkills(job.getRequiredSkills())
                .openings(job.getOpenings())
                .applicationDeadline(job.getApplicationDeadline())
                .recruiterId(recruiter != null ? recruiter.getId() : null)
                .companyName(recruiter != null ? recruiter.getCompanyName() : null)
                .companyLogoUrl(recruiter != null ? recruiter.getCompanyLogoUrl() : null)
                .companyLocation(recruiter != null ? recruiter.getCompanyLocation() : null)
                .companyWebsite(recruiter != null ? recruiter.getCompanyWebsite() : null)
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }

    private JobResponse mapToResponseWithEligibility(Job job, Double studentCgpa) {
        JobResponse response = mapToResponse(job);

        if (job.getMinCgpa() == null) {
            response.setIsEligible(true);
            response.setEligibilityReason("Eligible (No minimum CGPA required)");
        } else if (studentCgpa == null) {
            response.setIsEligible(false);
            response.setEligibilityReason("Please complete your profile and update your CGPA to check eligibility.");
        } else if (studentCgpa >= job.getMinCgpa()) {
            response.setIsEligible(true);
            response.setEligibilityReason("Eligible (Your CGPA: " + studentCgpa + " meets minimum " + job.getMinCgpa() + ")");
        } else {
            response.setIsEligible(false);
            response.setEligibilityReason("Not eligible (Requires minimum " + job.getMinCgpa() + " CGPA, your CGPA is " + studentCgpa + ")");
        }

        return response;
    }
}