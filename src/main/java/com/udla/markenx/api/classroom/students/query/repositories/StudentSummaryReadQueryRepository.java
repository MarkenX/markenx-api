package com.udla.markenx.api.classroom.students.query.repositories;

import com.udla.markenx.api.classroom.students.query.models.StudentDetailPortDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentSummaryReadQueryRepository {
    List<StudentDetailPortDTO> findAll();
    StudentDetailPortDTO findByIdOrThrow(String id);
    Optional<StudentDetailPortDTO> findById(String id);
    StudentDetailPortDTO findByEmail(String email);
    Page<StudentDetailPortDTO> findAllPaginated(Pageable pageable);
}