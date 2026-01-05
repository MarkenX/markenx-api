package com.udla.markenx.api.classroom.courses.application.ports.incoming;

import com.udla.markenx.api.classroom.courses.application.commands.SaveCourseCommand;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;

public interface SaveCourseUseCase {
    Course handle(SaveCourseCommand command);
}
