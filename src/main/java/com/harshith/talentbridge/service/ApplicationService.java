package com.harshith.talentbridge.service;

import com.harshith.talentbridge.dto.ApplicationRequest;
import com.harshith.talentbridge.dto.ApplicationResponse;
import com.harshith.talentbridge.dto.ApplicationStatusUpdateRequest;
import com.harshith.talentbridge.entity.Application;
import com.harshith.talentbridge.entity.Job;
import com.harshith.talentbridge.entity.RecruiterProfile;
import com.harshith.talentbridge.entity.StudentProfile;
import com.harshith.talentbridge.enums.ApplicationStatus;
import com.harshith.talentbridge.enums.JobStatus;
import com.harshith.talentbridge.repository.ApplicationRepository;
import com.harshith.talentbridge.repository.JobRepository;
import com.harshith.talentbridge.repository.RecruiterRepository;
import com.harshith.talentbridge.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final StudentRepository studentRepository;
    private final RecruiterRepository recruiterRepository;
    private final EmailService emailService; // <-- Injected Email Service

    @Transactional
    public ApplicationResponse applyForJob(String studentEmail, Long jobId, ApplicationRequest request) {
        StudentProfile student = studentRepository.findByUserEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student profile not found. Please complete your profile first."));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        if (job.getStatus() != JobStatus.OPEN && job.getStatus() != JobStatus.ACTIVE) {
            throw new RuntimeException("Cannot apply: This job posting is closed.");
        }

        if (job.getApplicationDeadline() != null && job.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot apply: The deadline for this job posting has expired.");
        }

        if (applicationRepository.existsByStudentAndJob(student, job)) {
            throw new RuntimeException("You have already applied for this job.");
        }

        if (job.getMinCgpa() != null) {
            if (student.getCgpa() == null) {
                throw new RuntimeException("Cannot apply: Please update your CGPA in your profile first.");
            }
            if (student.getCgpa() < job.getMinCgpa()) {
                throw new RuntimeException("Ineligible: Your CGPA (" + student.getCgpa() + ") is lower than the required minimum (" + job.getMinCgpa() + ").");
            }
        }

        String resume = (request != null && request.getResumeUrl() != null && !request.getResumeUrl().trim().isEmpty())
                ? request.getResumeUrl().trim()
                : student.getResumeUrl();

        Application application = Application.builder()
                .student(student)
                .job(job)
                .status(ApplicationStatus.APPLIED)
                .resumeUrl(resume)
                .coverLetter(request != null ? request.getCoverLetter() : null)
                .build();

        return mapToResponse(applicationRepository.save(application));
    }

    @Transactional
    public ApplicationResponse withdrawApplication(String studentEmail, Long applicationId) {
        StudentProfile student = studentRepository.findByUserEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student profile not found."));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + applicationId));

        if (!application.getStudent().getId().equals(student.getId())) {
            throw new RuntimeException("Access denied: You can only withdraw your own applications.");
        }

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new RuntimeException("Application is already withdrawn.");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        return mapToResponse(applicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications(String studentEmail) {
        StudentProfile student = studentRepository.findByUserEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student profile not found."));

        return applicationRepository.findByStudentOrderByAppliedAtDesc(student)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicantsForJob(String recruiterEmail, Long jobId) {
        validateRecruiterJobOwnership(recruiterEmail, jobId);

        Job job = jobRepository.findById(jobId).orElseThrow();
        return applicationRepository.findByJobOrderByAppliedAtDesc(job)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(String recruiterEmail, Long applicationId, ApplicationStatusUpdateRequest request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + applicationId));

        validateRecruiterJobOwnership(recruiterEmail, application.getJob().getId());

        application.setStatus(request.getStatus());

        if (request.getStatus() == ApplicationStatus.REJECTED) {
            application.setRecruiterFeedback(request.getRecruiterFeedback() != null ? request.getRecruiterFeedback().trim() : "Profile not aligned with current requirements.");
            application.setInterviewTime(null);
            application.setInterviewLink(null);
            application.setInterviewRound(null);

            Application saved = applicationRepository.save(application);
            emailService.sendRejectionEmail(saved); // <-- Trigger Rejection Email
            return mapToResponse(saved);
        }
        else if (request.getStatus() == ApplicationStatus.INTERVIEW_SCHEDULED) {
            application.setInterviewTime(request.getInterviewTime());
            application.setInterviewLink(request.getInterviewLink());
            application.setInterviewRound(request.getInterviewRound() != null ? request.getInterviewRound().trim() : "Technical Round 1");
            application.setRecruiterFeedback(request.getRecruiterFeedback());

            Application saved = applicationRepository.save(application);
            emailService.sendInterviewScheduledEmail(saved); // <-- Trigger Interview Email
            return mapToResponse(saved);
        }
        else if (request.getStatus() == ApplicationStatus.SHORTLISTED) {
            application.setRecruiterFeedback(request.getRecruiterFeedback());
            Application saved = applicationRepository.save(application);
            emailService.sendShortlistEmail(saved); // <-- Trigger Shortlist Email
            return mapToResponse(saved);
        }
        else {
            application.setRecruiterFeedback(request.getRecruiterFeedback());
            return mapToResponse(applicationRepository.save(application));
        }
    }

    private void validateRecruiterJobOwnership(String recruiterEmail, Long jobId) {
        RecruiterProfile recruiter = recruiterRepository.findByUserEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found."));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new RuntimeException("Access denied: You do not have permission to view applicants for this job.");
        }
    }

    private ApplicationResponse mapToResponse(Application application) {
        StudentProfile student = application.getStudent();
        Job job = application.getJob();

        return ApplicationResponse.builder()
                .id(application.getId())
                .studentId(student != null ? student.getId() : null)
                .studentName(student != null && student.getUser() != null ? student.getUser().getName() : null)
                .studentEmail(student != null && student.getUser() != null ? student.getUser().getEmail() : null)
                .studentBranch(student != null ? student.getBranch() : null)
                .studentUniversity(student != null ? student.getUniversity() : null)
                .studentCgpa(student != null ? student.getCgpa() : null)
                .studentSkills(student != null ? student.getSkills() : null)
                .jobId(job != null ? job.getId() : null)
                .jobTitle(job != null ? job.getTitle() : null)
                .companyName(job != null && job.getRecruiter() != null ? job.getRecruiter().getCompanyName() : null)
                .jobLocation(job != null ? job.getLocation() : null)
                .jobType(job != null ? job.getJobType() : null)
                .minCgpaRequired(job != null ? job.getMinCgpa() : null)
                .status(application.getStatus())
                .coverLetter(application.getCoverLetter())
                .resumeUrl(application.getResumeUrl())
                .recruiterFeedback(application.getRecruiterFeedback())
                .interviewTime(application.getInterviewTime())
                .interviewLink(application.getInterviewLink())
                .interviewRound(application.getInterviewRound())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}