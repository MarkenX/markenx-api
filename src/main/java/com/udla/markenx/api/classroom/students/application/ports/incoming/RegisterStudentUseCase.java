package com.udla.markenx.api.classroom.students.application.ports.incoming;

import com.udla.markenx.api.classroom.students.application.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;

public interface RegisterStudentUseCase {
    Student handle(RegisterStudentCommand command);
}
