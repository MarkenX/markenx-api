package com.udla.markenx.api.classroom.students.application.ports.out;

import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudentQueryRepository {
    Optional<Student> findById(String id);
    Student findByIdOrThrow(String id);
    Page<Student> findAllPaginated(Pageable pageable);
}
