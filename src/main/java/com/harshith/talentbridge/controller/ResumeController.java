package com.harshith.talentbridge.controller;

import com.harshith.talentbridge.dto.ResumeResponse;
import com.harshith.talentbridge.entity.Resume;
import com.harshith.talentbridge.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("versionNumber") int versionNumber,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "isDefault", required = false, defaultValue = "false") Boolean isDefault,
            @AuthenticationPrincipal UserDetails userDetails) {

        ResumeResponse response = resumeService.uploadResume(userDetails.getUsername(), versionNumber, title, isDefault, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my-resumes")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ResumeResponse>> getMyResumes(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(resumeService.getMyResumes(userDetails.getUsername()));
    }

    @GetMapping("/version/{versionNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ResumeResponse> getResumeByVersion(
            @PathVariable int versionNumber,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(resumeService.getResumeByVersion(userDetails.getUsername(), versionNumber));
    }

    @PatchMapping("/version/{versionNumber}/set-default")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ResumeResponse> setDefaultVersion(
            @PathVariable int versionNumber,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(resumeService.setDefaultVersion(userDetails.getUsername(), versionNumber));
    }

    @DeleteMapping("/version/{versionNumber}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, String>> deleteResumeVersion(
            @PathVariable int versionNumber,
            @AuthenticationPrincipal UserDetails userDetails) {
        resumeService.deleteResumeVersion(userDetails.getUsername(), versionNumber);
        return ResponseEntity.ok(Map.of("message", "Resume version " + versionNumber + " deleted successfully"));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<List<ResumeResponse>> getStudentResumesForRecruiter(@PathVariable Long studentId) {
        return ResponseEntity.ok(resumeService.getStudentResumesForRecruiter(studentId));
    }

    @GetMapping("/download/{resumeId}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long resumeId) {
        Resource resource = resumeService.loadResumeAsResource(resumeId);
        Resume resume = resumeService.getResumeEntity(resumeId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resume.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resume.getOriginalFileName() + "\"")
                .body(resource);
    }
}