package com.udla.markenx.api.classroom.students.application.ports.in.commands;

public record UpdateStudentCommand(
        String studentId,
        String firstName,
        String lastName,
        String courseId
) {
}
