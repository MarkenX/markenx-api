package com.udla.markenx.api.classroom.students.application.ports.out;

import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentQueryRepository {
    Optional<Student> findById(String id);
    Student findByIdOrThrow(String id);
    List<Student> findAll();
    Page<Student> findAllPaginated(Pageable pageable);
    List<Student> findByCourseId(String courseId);
}
