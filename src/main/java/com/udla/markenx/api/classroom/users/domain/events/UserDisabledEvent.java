package com.udla.markenx.api.classroom.users.domain.events;

public record UserDisabledEvent(
        String studentId,
        String userId
) {
}
