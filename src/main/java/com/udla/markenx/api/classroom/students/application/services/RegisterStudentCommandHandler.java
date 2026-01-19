package com.udla.markenx.api.classroom.students.application.services;

import com.udla.markenx.api.classroom.students.application.ports.in.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.RegisterStudentUseCase;
import com.udla.markenx.api.classroom.students.domain.events.StudentRegisteredEvent;
import com.udla.markenx.api.classroom.students.domain.exceptions.CourseNotInUpcomingTermException;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.IsUpcomingTermQuery;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.ValidateTermUseCase;
import com.udla.markenx.api.shared.domain.events.integration.IdentityProvisioningRequestedEvent;
import com.udla.markenx.api.classroom.students.domain.ports.outgoing.StudentCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterStudentCommandHandler implements RegisterStudentUseCase {

    private final ValidateTermUseCase validateTermUseCase;
    private final StudentCommandRepository repository;
    private final ApplicationEventPublisher events;

    private void ensureCourseTermIsUpcoming(@NonNull RegisterStudentCommand command) {
        var query = new IsUpcomingTermQuery(command.termId());
        if (!validateTermUseCase.isUpcoming(query)) {
            throw new CourseNotInUpcomingTermException(command.courseId());
        }
    }

    @Override
    public Student handle(@NonNull RegisterStudentCommand command) {
        if(!command.isHistorical()) {
            ensureCourseTermIsUpcoming(command);
        }

        Student newStudent = Student.create(
                command.firstName(),
                command.lastName(),
                command.courseId()
        );

        repository.save(newStudent);

        // Domain event for internal CQRS projections
        events.publishEvent(
                new StudentRegisteredEvent(
                        newStudent.getId(),
                        command.email(),
                        newStudent.getFullName()
                )
        );

        // Integration event for cross-module communication (identity provisioning)
        events.publishEvent(
                new IdentityProvisioningRequestedEvent(
                        newStudent.getId(),
                        command.email(),
                        newStudent.getFullName()
                )
        );

        return newStudent;
    }
}
