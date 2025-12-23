package com.udla.markenx.api.students.application.commands;

public record SaveStudentCommand(
        String firstName,
        String lastName,
        String email,
        String courseId
) {
}
