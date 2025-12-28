package com.udla.markenx.api.students.application.commands;

public record SaveStudentCommand(
        String firstName,
        String lastName,
        String courseId,
        String email
) {
}
