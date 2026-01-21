package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.application.dtos.StudentDetailPortDTO;
import org.springframework.data.domain.Page;

public interface QueryStudentsDetailUseCase {
    StudentDetailPortDTO findByEmail(String email);
    Page<StudentDetailPortDTO> listStudentsPage(StudentPageQueryCriteria query);
}
