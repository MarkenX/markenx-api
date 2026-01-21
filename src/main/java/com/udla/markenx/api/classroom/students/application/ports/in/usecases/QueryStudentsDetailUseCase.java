package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import org.springframework.data.domain.Page;

public interface QueryStudentsDetailUseCase {
    StudentSummaryReadModel findByEmail(String email);
    Page<StudentSummaryReadModel> listStudentsPage(StudentPageQueryCriteria query);
}
