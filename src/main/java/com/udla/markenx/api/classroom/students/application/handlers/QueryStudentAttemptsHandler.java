package com.udla.markenx.api.classroom.students.application.handlers;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentAttemptPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.mappers.StudentPortMapper;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAttemptsQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentAttemptsUseCase;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentAttemptQueryPort;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryStudentAttemptsHandler implements QueryStudentAttemptsUseCase {

    private final StudentAttemptQueryPort studentAttemptQueryPort;
    private final StudentPortMapper mapper = new StudentPortMapper();

    @Override
    public List<StudentAttemptPortDTO> getAll(@NonNull StudentAttemptsQuery query) {
        var attempts = studentAttemptQueryPort.findAttemptsByStudentId(query.studentId());
        return attempts.stream()
                .map(mapper::toStudentAttemptPortDTO)
                .toList();
    }
}
