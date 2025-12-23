package com.udla.markenx.api.students.application.ports.incoming;

import com.udla.markenx.api.students.application.queries.GetAllStudentsPaginatedQuery;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import org.springframework.data.domain.Page;

public interface StudentQueryUseCase {
    Page<Student> getAllPaginated(GetAllStudentsPaginatedQuery query);
}
