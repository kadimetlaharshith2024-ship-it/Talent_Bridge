package com.harshith.talentbridge.dto;

import com.harshith.talentbridge.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private ApplicationStatus status;
    private String recruiterFeedback;
    private LocalDateTime interviewTime;
    private String interviewLink;
    private String interviewRound;
}