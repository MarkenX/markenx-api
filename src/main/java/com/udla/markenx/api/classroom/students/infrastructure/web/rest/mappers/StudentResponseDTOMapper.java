package com.udla.markenx.api.classroom.students.infrastructure.web.rest.mappers;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentAttemptPortDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentAttemptResponseDTO;
import com.udla.markenx.api.classroom.students.infrastructure.web.rest.dtos.StudentResponseDTO;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StudentResponseDTOMapper {

    public StudentAttemptResponseDTO toAttemptResponseDTO(@NonNull StudentAttemptPortDTO port) {
        return new StudentAttemptResponseDTO(
                port.attemptId(),
                port.taskId(),
                port.startedAt(),
                port.finishedAt(),
                port.status(),
                port.outcome(),
                port.score()
        );
    }

    public StudentResponseDTO toDTO(@NonNull Student domain, String email) {
        return new StudentResponseDTO(
                domain.getId(),
                domain.toString(),
                domain.getFullName(),
                email,
                domain.getCourseId()
        );
    }
}
