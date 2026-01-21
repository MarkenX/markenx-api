package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

public record StudentProfileResponseDTO(
        String id,
        String email,
        String fullName,
        EnrolledCourseResponseDTO enrolledCourse,
        CurrentTermResponseDTO currentTerm
) {
    public record EnrolledCourseResponseDTO(
            String id,
            String label) {}
    public record CurrentTermResponseDTO(
            String id,
            String label) {}
}
