package com.udla.markenx.api.classroom.assignments.application.queries;

public record GetAllTasksPaginatedQuery(
    int page,
    int size
) {
}
