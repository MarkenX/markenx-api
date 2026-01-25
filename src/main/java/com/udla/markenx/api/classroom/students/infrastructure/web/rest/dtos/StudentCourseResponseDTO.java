package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

/**
 * Response DTO for the student's course information.
 * Used by GET /students/{attemptId}/course endpoint.
 */
public record StudentCourseResponseDTO(
        String courseId,
        String courseName,
        String term
) {
}
