package com.udla.markenx.api.students.application.ports.incoming;

import com.udla.markenx.api.students.application.commands.RegisterStudentCommand;
import com.udla.markenx.api.students.domain.models.aggregates.Student;

public interface RegisterStudentUseCase {
    Student handle(RegisterStudentCommand command);
}
