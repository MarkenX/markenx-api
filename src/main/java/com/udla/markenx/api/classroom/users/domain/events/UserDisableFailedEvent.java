package com.udla.markenx.api.classroom.users.domain.events;

public record UserDisableFailedEvent(
        String studentId,
        String message
) {
}
