package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentProfilePortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentProfileQuery;

public interface QueryStudentsProfileUseCase {
    StudentProfilePortDTO getByEmail(StudentProfileQuery query);
}
