package com.udla.markenx.api.classroom.students.query.repositories;

import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentSummaryPagedReadRepository {
    Page<StudentSummaryReadModel> findAllPaginated(Pageable pageable);
}