package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentsByCourseQuery;
import org.jspecify.annotations.NonNull;

import java.util.List;

public interface QueryStudentsByCourseUseCase {
    List<String> getStudentIdsByCourse(@NonNull StudentsByCourseQuery query);
}
