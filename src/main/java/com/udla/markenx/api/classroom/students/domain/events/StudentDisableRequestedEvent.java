package com.udla.markenx.api.classroom.students.domain.events;

public record StudentDisableRequestedEvent(
        String studentId,
        String userId,
        String email
) {
}
