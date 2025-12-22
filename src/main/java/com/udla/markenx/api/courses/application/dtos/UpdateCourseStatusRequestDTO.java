package com.udla.markenx.api.courses.application.dtos;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record UpdateCourseStatusRequestDTO(LifecycleStatus status) {
}
