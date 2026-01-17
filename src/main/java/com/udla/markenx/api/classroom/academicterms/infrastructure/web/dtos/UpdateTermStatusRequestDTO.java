package com.udla.markenx.api.classroom.academicterms.infrastructure.web.dtos;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record UpdateTermStatusRequestDTO(LifecycleStatus status) {
}
