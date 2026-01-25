package com.udla.markenx.api.classroom.students.application.ports.in.mappers;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentAttemptPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentAttemptQueryPort.StudentAttemptData;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
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

    public StudentAttemptPortDTO toStudentAttemptPortDTO(@NonNull StudentAttemptData data) {
        return new StudentAttemptPortDTO(
                data.attemptId(),
                data.taskId(),
                data.evaluatedAt(),
                data.evaluatedAt(),
                data.finalOutcome(),
                data.finalOutcome(),
                data.finalAcceptance());
    }
}
