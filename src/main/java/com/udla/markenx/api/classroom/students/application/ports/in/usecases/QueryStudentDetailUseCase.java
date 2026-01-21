package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentDetailPortDTO;
import org.springframework.data.domain.Page;

public interface QueryStudentDetailUseCase {
    StudentDetailPortDTO findByEmail(String email);
    Page<StudentDetailPortDTO> listStudentsPage(StudentPageQueryCriteria query);
}
