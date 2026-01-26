package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentPortDTO;

import java.util.List;

public interface QueryStudentsUseCase {
    StudentPortDTO getStudentById(String studentId);
    List<StudentPortDTO> listStudents();
}
