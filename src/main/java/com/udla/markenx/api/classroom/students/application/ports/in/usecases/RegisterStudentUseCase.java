package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;

public interface RegisterStudentUseCase {
    Student handle(RegisterStudentCommand command);
}
