package com.udla.markenx.api.academicterms.application.dtos;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record ChangeAcademicTermStatusRequestDTO(LifecycleStatus status) {
}
