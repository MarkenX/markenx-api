package com.udla.markenx.api.classroom.courses.application.ports.in.usecases;

import com.udla.markenx.api.classroom.courses.application.ports.in.commands.SaveCourseCommand;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;

public interface SaveCourseUseCase {
    Course handle(SaveCourseCommand command);
}
