package com.udla.markenx.api.users.domain.events;

public record UserCreationFailedEvent(
        String studentId,
        String message
) {
}