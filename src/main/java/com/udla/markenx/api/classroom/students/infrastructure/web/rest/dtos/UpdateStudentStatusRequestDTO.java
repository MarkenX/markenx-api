package com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record UpdateStudentStatusRequestDTO(LifecycleStatus status) {
}
