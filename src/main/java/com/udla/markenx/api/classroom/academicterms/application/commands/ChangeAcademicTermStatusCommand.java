package com.udla.markenx.api.classroom.academicterms.application.commands;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record ChangeAcademicTermStatusCommand(String id, LifecycleStatus targetStatus) {
}
