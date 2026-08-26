package com.harshith.talentbridge.dto;

import com.harshith.talentbridge.enums.VerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterVerificationRequest {

    @NotNull(message = "Verification status is required (APPROVED, REJECTED, PENDING)")
    private VerificationStatus status;

    private String notes;
}