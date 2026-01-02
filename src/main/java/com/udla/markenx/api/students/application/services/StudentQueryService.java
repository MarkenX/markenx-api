package com.udla.markenx.api.students.application.services;

import com.udla.markenx.api.students.infrastructure.web.rest.dtos.StudentUserReadDTO;
import com.udla.markenx.api.students.application.ports.incoming.StudentQueryUseCase;
import com.udla.markenx.api.students.application.queries.GetAllStudentsPaginatedQuery;
import com.udla.markenx.api.students.domain.ports.outgoing.StudentQueryRepository;
import com.udla.markenx.api.students.query.models.StudentSummaryReadModel;
import com.udla.markenx.api.students.query.repositories.StudentSummaryPagedReadRepository;
import com.udla.markenx.api.students.query.repositories.StudentSummaryReadRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentQueryService implements StudentQueryUseCase {

    private final StudentSummaryPagedReadRepository repository;

    @Override
    public Page<StudentSummaryReadModel> getAllPaginated(@NonNull GetAllStudentsPaginatedQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return repository.findAllPaginated(pageable);
    }
}
