package com.udla.markenx.api.classroom.assignments.infrastructure.web.dtos.requests;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record UpdateTaskStatusRequestDTO(LifecycleStatus status) {
}
