package com.harshith.talentbridge.service;

import com.harshith.talentbridge.dto.ResumeResponse;
import com.harshith.talentbridge.entity.Resume;
import com.harshith.talentbridge.entity.StudentProfile;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.repository.ResumeRepository;
import com.harshith.talentbridge.repository.StudentRepository;
import com.harshith.talentbridge.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir:uploads/resumes}")
    private String uploadDir;

    private Path fileStorageLocation;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create directory where uploaded files will be stored.", ex);
        }
    }

    @Transactional
    public ResumeResponse uploadResume(String studentEmail, int versionNumber, String title, Boolean isDefault, MultipartFile file) {
        if (versionNumber < 1 || versionNumber > 3) {
            throw new RuntimeException("Invalid version number. You can only maintain Version 1, Version 2, or Version 3.");
        }

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Please upload a non-empty file.");
        }

        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String lowerName = originalFileName.toLowerCase();

        boolean isValidExtension = lowerName.endsWith(".pdf") || lowerName.endsWith(".docx");
        String contentType = file.getContentType();
        boolean isValidContentType = contentType != null && (
                contentType.equalsIgnoreCase("application/pdf") ||
                        contentType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                        contentType.equalsIgnoreCase("application/octet-stream")
        );

        if (!isValidExtension && !isValidContentType) {
            throw new RuntimeException("Only PDF (.pdf) and Word (.docx) documents are supported.");
        }

        StudentProfile student = getStudentByEmail(studentEmail);
        String fileExtension = originalFileName.contains(".") ? originalFileName.substring(originalFileName.lastIndexOf(".")) : ".pdf";
        String generatedFileName = "resume_s" + student.getId() + "_v" + versionNumber + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

        // Ensure detected content type defaults to application/pdf if octet-stream was supplied
        String resolvedContentType = (contentType == null || contentType.equalsIgnoreCase("application/octet-stream"))
                ? (lowerName.endsWith(".docx") ? "application/vnd.openxmlformats-officedocument.wordprocessingml.document" : "application/pdf")
                : contentType;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(generatedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Check if this version slot already exists (replace/update workflow)
            Resume resume = resumeRepository.findByStudentAndVersionNumber(student, versionNumber)
                    .orElse(Resume.builder()
                            .student(student)
                            .versionNumber(versionNumber)
                            .build());

            // If an older file was attached to this version slot, delete it from disk
            if (resume.getStoragePath() != null) {
                try {
                    Files.deleteIfExists(Paths.get(resume.getStoragePath()));
                } catch (Exception ignored) {}
            }

            if (Boolean.TRUE.equals(isDefault)) {
                resetDefaultResumes(student);
                resume.setIsDefault(true);
            } else if (resume.getIsDefault() == null) {
                // If it's the first resume being uploaded, make it default automatically
                resume.setIsDefault(resumeRepository.countByStudent(student) == 0);
            }

            resume.setOriginalFileName(originalFileName);
            resume.setFileName(generatedFileName);
            resume.setContentType(resolvedContentType);
            resume.setFileSize(file.getSize());
            resume.setStoragePath(targetLocation.toString());
            resume.setTitle(title != null && !title.trim().isEmpty() ? title.trim() : "Resume Version " + versionNumber);

            Resume saved = resumeRepository.save(resume);

            // Sync latest resume URL to Student Profile if marked as default
            if (Boolean.TRUE.equals(saved.getIsDefault())) {
                student.setResumeUrl("/api/resumes/download/" + saved.getId());
                studentRepository.save(student);
            }

            return mapToResponse(saved);

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    @Transactional(readOnly = true)
    public List<ResumeResponse> getMyResumes(String studentEmail) {
        StudentProfile student = getStudentByEmail(studentEmail);
        return resumeRepository.findByStudentOrderByVersionNumberAsc(student)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResumeResponse getResumeByVersion(String studentEmail, int versionNumber) {
        StudentProfile student = getStudentByEmail(studentEmail);
        Resume resume = resumeRepository.findByStudentAndVersionNumber(student, versionNumber)
                .orElseThrow(() -> new RuntimeException("Version " + versionNumber + " resume not found."));
        return mapToResponse(resume);
    }

    @Transactional
    public ResumeResponse setDefaultVersion(String studentEmail, int versionNumber) {
        StudentProfile student = getStudentByEmail(studentEmail);
        Resume resume = resumeRepository.findByStudentAndVersionNumber(student, versionNumber)
                .orElseThrow(() -> new RuntimeException("Version " + versionNumber + " resume not found."));

        resetDefaultResumes(student);
        resume.setIsDefault(true);
        Resume saved = resumeRepository.save(resume);

        student.setResumeUrl("/api/resumes/download/" + saved.getId());
        studentRepository.save(student);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ResumeResponse> getStudentResumesForRecruiter(Long studentId) {
        StudentProfile student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student profile not found with ID: " + studentId));

        return resumeRepository.findByStudentOrderByVersionNumberAsc(student)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Resource loadResumeAsResource(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found with ID: " + resumeId));

        try {
            Path filePath = Paths.get(resume.getStoragePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or unreadable on server disk.");
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File path error.", ex);
        }
    }

    @Transactional(readOnly = true)
    public Resume getResumeEntity(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found with ID: " + resumeId));
    }

    @Transactional
    public void deleteResumeVersion(String studentEmail, int versionNumber) {
        StudentProfile student = getStudentByEmail(studentEmail);
        Resume resume = resumeRepository.findByStudentAndVersionNumber(student, versionNumber)
                .orElseThrow(() -> new RuntimeException("Version " + versionNumber + " resume not found."));

        try {
            Files.deleteIfExists(Paths.get(resume.getStoragePath()));
        } catch (Exception ignored) {}

        resumeRepository.delete(resume);
    }

    private void resetDefaultResumes(StudentProfile student) {
        List<Resume> resumes = resumeRepository.findByStudentOrderByVersionNumberAsc(student);
        for (Resume r : resumes) {
            if (Boolean.TRUE.equals(r.getIsDefault())) {
                r.setIsDefault(false);
                resumeRepository.save(r);
            }
        }
    }

    private StudentProfile getStudentByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found. Please complete your student profile first."));
    }

    private ResumeResponse mapToResponse(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .studentId(resume.getStudent().getId())
                .studentName(resume.getStudent().getUser().getName())
                .versionNumber(resume.getVersionNumber())
                .title(resume.getTitle())
                .originalFileName(resume.getOriginalFileName())
                .contentType(resume.getContentType())
                .fileSize(resume.getFileSize())
                .isDefault(resume.getIsDefault())
                .downloadUrl("/api/resumes/download/" + resume.getId())
                .uploadedAt(resume.getUploadedAt())
                .build();
    }
}