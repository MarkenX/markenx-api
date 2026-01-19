package com.udla.markenx.api.classroom.assignments.application.ports.in.queries;

public record GetAllTasksPaginatedQuery(
    int page,
    int size
) {
}
