package com.udla.markenx.api.students.application.services;

import com.udla.markenx.api.students.application.dtos.StudentUserReadDTO;
import com.udla.markenx.api.students.application.ports.incoming.StudentQueryUseCase;
import com.udla.markenx.api.students.application.queries.GetAllStudentsPaginatedQuery;
import com.udla.markenx.api.students.domain.ports.outgoing.StudentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentQueryService implements StudentQueryUseCase {

    private final StudentQueryRepository repository;

    @Override
    public Page<StudentUserReadDTO> getAllPaginated(@NonNull GetAllStudentsPaginatedQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return repository.findAllPaginated(pageable);
    }
}
