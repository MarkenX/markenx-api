package com.udla.markenx.api.classroom.students.domain.events;

public record StudentUpdatedEvent(
        String studentId,
        String fullName,
        String courseId
) {
}
