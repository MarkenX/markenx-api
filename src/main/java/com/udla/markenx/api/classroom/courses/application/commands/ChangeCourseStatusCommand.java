package com.udla.markenx.api.classroom.courses.application.commands;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

public record ChangeCourseStatusCommand(String id, LifecycleStatus targetStatus) {
}
