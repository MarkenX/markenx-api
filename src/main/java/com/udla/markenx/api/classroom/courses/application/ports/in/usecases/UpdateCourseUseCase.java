package com.udla.markenx.api.classroom.courses.application.ports.in.usecases;

import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeCourseAcademicTermCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.ChangeCourseStatusCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.commands.UpdateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.GetCourseByIdQuery;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;

public interface UpdateCourseUseCase {
    Course changeAcademicTerm(ChangeCourseAcademicTermCommand command);
    Course changeStatus(ChangeCourseStatusCommand command);
    Course update(UpdateCourseCommand command);
    Course getById(GetCourseByIdQuery query);
}
