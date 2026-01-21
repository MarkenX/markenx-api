package com.udla.markenx.api.classroom.students.application.ports.out;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentDetailPortDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentDetailQueryRepository {
    List<StudentDetailPortDTO> findAll();
    StudentDetailPortDTO findByIdOrThrow(String id);
    Optional<StudentDetailPortDTO> findById(String id);
    StudentDetailPortDTO findByEmail(String email);
    Page<StudentDetailPortDTO> findAllPaginated(Pageable pageable);
}