package com.udla.markenx.api.classroom.courses.application.ports.incoming;

import com.udla.markenx.api.classroom.courses.application.commands.ChangeCourseAcademicTermCommand;
import com.udla.markenx.api.classroom.courses.application.commands.ChangeCourseStatusCommand;
import com.udla.markenx.api.classroom.courses.application.commands.UpdateCourseCommand;
import com.udla.markenx.api.classroom.courses.application.queries.GetCourseByIdQuery;
import com.udla.markenx.api.classroom.courses.domain.models.aggregates.Course;

public interface UpdateCourseUseCase {
    Course changeAcademicTerm(ChangeCourseAcademicTermCommand command);
    Course changeStatus(ChangeCourseStatusCommand command);
    Course update(UpdateCourseCommand command);
    Course getById(GetCourseByIdQuery query);
}
