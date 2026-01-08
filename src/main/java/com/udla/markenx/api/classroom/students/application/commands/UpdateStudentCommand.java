package com.udla.markenx.api.classroom.students.application.commands;

public record UpdateStudentCommand(
        String studentId,
        String firstName,
        String lastName,
        String courseId
) {
}
