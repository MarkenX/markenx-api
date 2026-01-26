package com.udla.markenx.api.classroom.students.application.ports.in.commands;

public record RegisterStudentCommand(
        String firstName,
        String lastName,
        String courseId,
        String email,
        boolean isHistorical
) {
}
