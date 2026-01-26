package com.udla.markenx.api.classroom.students.application.ports.in.dtos;

public record StudentDetailPortDTO(
        String studentId,
        String email,
        String fullName,
        int code,
        String courseId,
        String lifecycleStatus
) {
    /**
     * Returns the student label in format "STD-XXXX".
     */
    public String label() {
        return String.format("STD-%04d", code);
    }
}
