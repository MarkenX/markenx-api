package com.udla.markenx.api.classroom.courses.application.ports.in.usecases;

import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeTermCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeStatusCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.UpdateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseIdQueryCriteria;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;

public interface UpdateCourseUseCase {
    CoursePortDTO changeTerm(ChangeTermCommand command);
    CoursePortDTO changeStatus(ChangeStatusCommand command);
    CoursePortDTO update(UpdateCourseCommand command);
    CoursePortDTO getById(CourseIdQueryCriteria query);
}
