package com.udla.markenx.api.classroom.users.domain.events;

public record UserCreationFailedEvent(
        String studentId,
        String message
) {
}