package com.udla.markenx.api.classroom.courses.application.ports.in.commands;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record ChangeStatusCommand(String id, LifecycleStatus targetStatus) {
}
