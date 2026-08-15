package com.gwpriso.click_and_collect_backend.staff;

import java.util.UUID;

public record StaffResponse(
        UUID id,
        String email,
        RoleStaff role
) {
    public static StaffResponse from(UtilisateurStaff staff) {
        return new StaffResponse(staff.getId(), staff.getEmail(), staff.getRole());
    }
}