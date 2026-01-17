package com.udla.markenx.api.classroom.academicterms.application.ports.in.commands;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record ChangeTermStatusCommand(String id, LifecycleStatus targetStatus) {
}
