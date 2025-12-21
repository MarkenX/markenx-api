package com.udla.markenx.api.courses.application.ports.incoming;

import com.udla.markenx.api.courses.application.commands.ChangeCourseAcademicTermCommand;
import com.udla.markenx.api.courses.application.commands.ChangeCourseStatusCommand;
import com.udla.markenx.api.courses.application.queries.GetCourseByIdQuery;
import com.udla.markenx.api.courses.domain.models.aggregates.Course;

public interface UpdateCourseUseCase {
    Course changeAcademicTerm(ChangeCourseAcademicTermCommand command);
    Course changeStatus(ChangeCourseStatusCommand command);
    Course getById(GetCourseByIdQuery query);
}
