package com.udla.markenx.api.classroom.students.domain.events;

public record StudentRegisteredEvent(
        String studentId,
        String email,
        String fullName
) {
}
