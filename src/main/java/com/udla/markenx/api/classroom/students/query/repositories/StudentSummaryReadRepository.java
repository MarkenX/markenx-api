package com.udla.markenx.api.classroom.students.query.repositories;

import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;

import java.util.List;
import java.util.Optional;

public interface StudentSummaryReadRepository {
    void upsert(StudentSummaryReadModel model);
    Optional<StudentSummaryReadModel> findByStudentId(String studentId);
    List<StudentSummaryReadModel> findAll();
}