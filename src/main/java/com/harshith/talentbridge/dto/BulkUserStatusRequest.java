package com.harshith.talentbridge.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkUserStatusRequest {
    @NotEmpty(message = "User IDs list cannot be empty")
    private List<Long> userIds;

    @NotNull(message = "Enabled flag must be true or false")
    private Boolean enabled;
}