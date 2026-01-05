package com.udla.markenx.api.classroom.academicterms.application.dtos;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record UpdateAcademicTermStatusRequestDTO(LifecycleStatus status) {
}
