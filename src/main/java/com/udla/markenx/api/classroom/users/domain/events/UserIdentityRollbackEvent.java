package com.udla.markenx.api.classroom.users.domain.events;

public record UserIdentityRollbackEvent(
        String userId,
        String reason
) {}
