package com.udla.markenx.api.classroom.students.application.services;

import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentDetailUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentDetailPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentDetailQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentQueryServiceDetail implements QueryStudentDetailUseCase {

    private final StudentDetailQueryRepository pagedRepository;

    @Override
    public Page<StudentDetailPortDTO> listStudentsPage(@NonNull StudentPageQueryCriteria query) {
        var pageable = PageRequest.of(query.page(), query.size());
        return pagedRepository.findAllPaginated(pageable);
    }

    @Override
    public StudentDetailPortDTO findByEmail(String email) {
        return pagedRepository.findByEmail(email);
    }
}
