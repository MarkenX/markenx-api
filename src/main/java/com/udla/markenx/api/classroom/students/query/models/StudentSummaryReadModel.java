package com.udla.markenx.api.classroom.students.query.models;

public record StudentSummaryReadModel(
        String studentId,
        String email,
        String fullName
) {}
