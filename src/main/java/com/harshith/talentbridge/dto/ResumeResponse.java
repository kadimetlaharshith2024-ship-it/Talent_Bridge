package com.harshith.talentbridge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Integer versionNumber;
    private String title;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private Boolean isDefault;
    private String downloadUrl;
    private LocalDateTime uploadedAt;
}