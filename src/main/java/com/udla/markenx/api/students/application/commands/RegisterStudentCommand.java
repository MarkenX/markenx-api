package com.udla.markenx.api.students.application.commands;

public record RegisterStudentCommand(
        String firstName,
        String lastName,
        String courseId,
        String email
) {
}
