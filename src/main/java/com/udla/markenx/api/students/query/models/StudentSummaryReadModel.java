package com.udla.markenx.api.students.query.models;

public record StudentSummaryReadModel(
        String studentId,
        String email,
        String fullName
) {}
