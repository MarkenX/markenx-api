package com.udla.markenx.api.students.application.services;

import com.udla.markenx.api.students.application.commands.RegisterStudentCommand;
import com.udla.markenx.api.students.application.ports.incoming.CourseValidation;
import com.udla.markenx.api.students.application.ports.incoming.RegisterStudentUseCase;
import com.udla.markenx.api.students.domain.events.StudentRegisteredEvent;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import com.udla.markenx.api.students.domain.ports.outgoing.StudentCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterStudentCommandHandler implements RegisterStudentUseCase {

    private final CourseValidation courseValidation;
    private final StudentCommandRepository repository;
    private final ApplicationEventPublisher events;

    @Override
    public Student handle(@NonNull RegisterStudentCommand command) {
        courseValidation.ensureCourseExists(command.courseId());

        Student newStudent = Student.create(
                command.firstName(),
                command.lastName(),
                command.courseId()
        );

        repository.save(newStudent);

        events.publishEvent(
                new StudentRegisteredEvent(newStudent.getId(), command.email(), newStudent.getFullName())
        );

        return newStudent;
    }
}
