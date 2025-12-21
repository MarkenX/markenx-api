package com.udla.markenx.api.courses.application.commands;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record ChangeCourseStatusCommand(String id, LifecycleStatus targetStatus) {
}
