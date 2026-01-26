package com.udla.markenx.api.classroom.students.application.services;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.mappers.StudentPortMapper;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentsUseCase;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryStudentsService implements QueryStudentsUseCase {

    private final StudentQueryRepository repository;

    private final StudentPortMapper mapper = new StudentPortMapper();

    @Override
    public StudentPortDTO getStudentById(String studentId) {
        return mapper.toStudentPortDTO(repository.findByIdOrThrow(studentId));
    }

    @Override
    public List<StudentPortDTO> listStudents() {
        return repository.findAll().stream()
                .map(mapper::toStudentPortDTO)
                .toList();
    }
}
