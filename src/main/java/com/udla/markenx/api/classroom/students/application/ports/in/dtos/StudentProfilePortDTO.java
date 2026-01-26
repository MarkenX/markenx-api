package com.udla.markenx.api.classroom.students.application.ports.in.dtos;

public record StudentProfilePortDTO(
        String id,
        String email,
        String fullName,
        EnrolledCoursePortDTO enrolledCourse,
        CurrentTermPortDTO currentTerm
) {
    public record EnrolledCoursePortDTO(
            String id,
            String label) {}
    public record CurrentTermPortDTO(
            String id,
            String label) {}
}
