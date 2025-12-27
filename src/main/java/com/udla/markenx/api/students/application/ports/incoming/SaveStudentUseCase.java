package com.udla.markenx.api.students.application.ports.incoming;

import com.udla.markenx.api.users.application.commands.CreateUserCommand;
import com.udla.markenx.api.students.domain.models.aggregates.Student;

public interface SaveStudentUseCase {
    Student handle(CreateUserCommand command);
}
