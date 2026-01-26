package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.requests;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record UpdateStudentStatusRequestDTO(LifecycleStatus status) {
}
