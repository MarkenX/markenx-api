package com.udla.markenx.api.classroom.students.application.services;

import com.udla.markenx.api.classroom.students.application.ports.incoming.StudentQueryUseCase;
import com.udla.markenx.api.classroom.students.application.queries.GetAllStudentsPaginatedQuery;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import com.udla.markenx.api.classroom.students.query.repositories.StudentSummaryPagedReadRepository;
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
