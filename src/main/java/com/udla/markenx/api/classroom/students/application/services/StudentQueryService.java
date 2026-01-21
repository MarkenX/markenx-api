package com.udla.markenx.api.classroom.students.application.services;

import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentsUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import com.udla.markenx.api.classroom.students.query.repositories.StudentSummaryReadQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentQueryService implements QueryStudentsUseCase {

    private final StudentSummaryReadQueryRepository pagedRepository;

    @Override
    public Page<StudentSummaryReadModel> listStudentsPage(@NonNull StudentPageQueryCriteria query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return pagedRepository.findAllPaginated(pageable);
    }

    @Override
    public StudentSummaryReadModel findByEmail(String email) {
        return pagedRepository.findByEmail(email);
    }
}
