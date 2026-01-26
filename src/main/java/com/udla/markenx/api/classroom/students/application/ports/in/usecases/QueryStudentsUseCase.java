package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentIdQuery;

import java.util.List;

public interface QueryStudentsUseCase {
    StudentPortDTO getStudentById(StudentIdQuery query);
    List<StudentPortDTO> listStudents();
}
