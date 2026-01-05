package com.udla.markenx.api.classroom.courses.application.dtos;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record UpdateCourseStatusRequestDTO(LifecycleStatus status) {
}
