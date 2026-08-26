package com.harshith.talentbridge.dto;

import com.harshith.talentbridge.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleUpdateRequest {
    @NotNull(message = "Role is required (STUDENT, RECRUITER, ADMIN)")
    private Role role;
}