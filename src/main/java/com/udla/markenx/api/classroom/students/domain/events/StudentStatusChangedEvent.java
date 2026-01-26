package com.udla.markenx.api.classroom.students.domain.events;

public record StudentStatusChangedEvent(
        String studentId,
        String lifecycleStatus
) {
}
