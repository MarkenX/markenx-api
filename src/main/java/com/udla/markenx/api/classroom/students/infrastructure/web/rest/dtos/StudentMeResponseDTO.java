package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

/**
 * Response DTO for the authenticated student's profile.
 * Used by GET /students/me endpoint.
 */
public record StudentMeResponseDTO(
        String studentId,
        String email,
        String fullName
) {
}
