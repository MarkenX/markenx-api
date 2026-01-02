package com.udla.markenx.api.students.domain.events;

public record StudentRegisteredEvent(
        String studentId,
        String email
) {
}
