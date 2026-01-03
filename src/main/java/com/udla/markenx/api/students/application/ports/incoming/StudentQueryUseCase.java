package com.udla.markenx.api.students.application.ports.incoming;

import com.udla.markenx.api.students.application.queries.GetAllStudentsPaginatedQuery;
import com.udla.markenx.api.students.query.models.StudentSummaryReadModel;
import org.springframework.data.domain.Page;

public interface StudentQueryUseCase {
    Page<StudentSummaryReadModel> getAllPaginated(GetAllStudentsPaginatedQuery query);
}
