package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.game.attempts.application.ports.in.dtos.AttemptPortDTO;
import org.springframework.data.domain.Page;

public interface QueryStudentAttemptsUseCase {
    Page<AttemptPortDTO> getAllPaginated(StudentPageQueryCriteria query);
}
