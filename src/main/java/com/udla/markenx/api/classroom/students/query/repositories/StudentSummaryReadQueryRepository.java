package com.udla.markenx.api.classroom.students.query.repositories;

import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentSummaryReadQueryRepository {
    List<StudentSummaryReadModel> findAll();
    StudentSummaryReadModel findByIdOrThrow(String id);
    Optional<StudentSummaryReadModel> findById(String id);
    StudentSummaryReadModel findByEmail(String email);
    Page<StudentSummaryReadModel> findAllPaginated(Pageable pageable);
}