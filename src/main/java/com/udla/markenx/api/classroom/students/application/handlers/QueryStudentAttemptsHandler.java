package com.udla.markenx.api.classroom.students.application.handlers;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentAttemptPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.mappers.StudentPortMapper;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAttemptsQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentAttemptsUseCase;
import com.udla.markenx.api.game.attempts.application.ports.in.dtos.AttemptPortDTO;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.AttemptQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryStudentAttemptsHandler implements QueryStudentAttemptsUseCase {

    private final AttemptQueryUseCase attemptQueryUseCase;
    private final StudentPortMapper mapper = new StudentPortMapper();

    @Override
    public List<StudentAttemptPortDTO> getAll(StudentAttemptsQuery query) {
        List<AttemptPortDTO> attempts = attemptQueryUseCase.getByStudentId(query.studentId());
        return attempts.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
