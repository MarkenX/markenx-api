package com.udla.markenx.api.users.domain.events;

public record UserCreatedEvent(
        String studentId,
        String userId
) {
}
