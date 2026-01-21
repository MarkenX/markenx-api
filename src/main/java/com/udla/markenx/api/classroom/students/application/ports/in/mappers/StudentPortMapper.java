package com.udla.markenx.api.classroom.students.application.ports.in.mappers;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentAttemptPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentPortDTO;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import com.udla.markenx.api.game.attempts.application.ports.in.dtos.AttemptPortDTO;
import org.jspecify.annotations.NonNull;

public class StudentPortMapper {

    public StudentPortDTO toStudentPortDTO(@NonNull Student domain) {
        return new StudentPortDTO(
                domain.getId(),
                domain.toString(),
                domain.getFullName(),
                domain.getCourseId()
        );
    }

    public StudentAttemptPortDTO toStudentAttemptPortDTO(@NonNull AttemptPortDTO port) {
        return new StudentAttemptPortDTO(
                port.attemptId(),
                port.taskId(),
                port.evaluatedAt(),
                port.evaluatedAt(),
                port.finalOutcome(),
                port.finalOutcome(),
                port.finalAcceptance());
    }
}
