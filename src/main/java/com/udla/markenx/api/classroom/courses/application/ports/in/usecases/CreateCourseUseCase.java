package com.udla.markenx.api.classroom.courses.application.ports.in.usecases;

import com.udla.markenx.api.classroom.courses.application.ports.in.commands.CreateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;

public interface CreateCourseUseCase {
    CoursePortDTO handle(CreateCourseCommand command);
}
