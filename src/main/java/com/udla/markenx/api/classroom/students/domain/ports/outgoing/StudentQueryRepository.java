package com.udla.markenx.api.classroom.students.domain.ports.outgoing;

import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentUserReadDTO;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentQueryRepository {
    Student findById(String id);
    Page<StudentUserReadDTO> findAllPaginated(Pageable pageable);
}
