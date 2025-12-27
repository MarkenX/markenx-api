package com.udla.markenx.api.students.domain.ports.outgoing;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentQueryRepository {
    Student findById(String id);
    Page<Student> findAllPaginated(Pageable pageable);
}
