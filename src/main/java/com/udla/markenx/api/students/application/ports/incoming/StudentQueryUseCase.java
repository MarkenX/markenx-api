package com.udla.markenx.api.students.application.ports.incoming;

import com.udla.markenx.api.students.infrastructure.web.rest.dtos.StudentUserReadDTO;
import com.udla.markenx.api.students.application.queries.GetAllStudentsPaginatedQuery;
import org.springframework.data.domain.Page;

public interface StudentQueryUseCase {
    Page<StudentUserReadDTO> getAllPaginated(GetAllStudentsPaginatedQuery query);
}
