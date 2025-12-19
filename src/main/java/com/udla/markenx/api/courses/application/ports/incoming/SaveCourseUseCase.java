package com.udla.markenx.api.courses.application.ports.incoming;

import com.udla.markenx.api.courses.application.commands.SaveCourseCommand;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;

public interface SaveCourseUseCase {
    Course handle(SaveCourseCommand command);
}
