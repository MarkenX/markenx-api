package com.udla.markenx.api.students.application.ports.incoming;

import com.udla.markenx.api.students.application.commands.SaveStudentCommand;
import com.udla.markenx.api.students.domain.models.aggregates.Student;

public interface SaveStudentUseCase {
    Student handle(SaveStudentCommand command);
}
