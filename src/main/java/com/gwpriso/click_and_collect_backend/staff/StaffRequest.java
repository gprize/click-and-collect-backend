package com.gwpriso.click_and_collect_backend.staff;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StaffRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String motDePasse,
        @NotNull RoleStaff role
) {
}