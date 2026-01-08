package com.udla.markenx.api.classroom.students.application.services;

import com.udla.markenx.api.classroom.students.application.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.incoming.EnsureCourseHasUpcomingTerm;
import com.udla.markenx.api.classroom.students.application.ports.incoming.RegisterStudentUseCase;
import com.udla.markenx.api.classroom.students.domain.events.StudentRegisteredEvent;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import com.udla.markenx.api.classroom.students.domain.ports.outgoing.StudentCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterStudentCommandHandler implements RegisterStudentUseCase {

    private final EnsureCourseHasUpcomingTerm ensureCourseHasUpcomingTerm;
    private final StudentCommandRepository repository;
    private final ApplicationEventPublisher events;

    @Override
    public Student handle(@NonNull RegisterStudentCommand command) {
        if (!command.isHistorical()) {
            ensureCourseHasUpcomingTerm.ensureCourseHasUpcomingTerm(command.courseId());
        }

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
