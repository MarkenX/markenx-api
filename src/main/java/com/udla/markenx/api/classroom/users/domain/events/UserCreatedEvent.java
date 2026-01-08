package com.udla.markenx.api.classroom.users.domain.events;

public record UserCreatedEvent(
        String studentId,
        String userId
) {
}
