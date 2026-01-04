package com.udla.markenx.api.assignments.application.queries;

public record GetAllTasksPaginatedQuery(
    int page,
    int size
) {
}
