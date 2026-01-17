package com.udla.markenx.api.classroom.courses.infrastructure.web.dtos;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record UpdateCourseStatusRequestDTO(LifecycleStatus status) {
}
