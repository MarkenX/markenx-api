package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentAttemptPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAttemptsQuery;

import java.util.List;

public interface QueryStudentAttemptsUseCase {
    List<StudentAttemptPortDTO> getAll(StudentAttemptsQuery query);
}
