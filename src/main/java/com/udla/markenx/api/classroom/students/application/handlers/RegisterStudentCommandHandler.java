package com.udla.markenx.api.classroom.students.application.handlers;

import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.CourseIdQuery;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.QueryCourseUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.commands.RegisterStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.RegisterStudentUseCase;
import com.udla.markenx.api.classroom.students.domain.events.StudentRegisteredEvent;
import com.udla.markenx.api.classroom.students.domain.exceptions.CourseNotInUpcomingTermException;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.IsUpcomingTermQuery;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.ValidateTermUseCase;
import com.udla.markenx.api.shared.domain.events.integration.IdentityProvisioningRequestedEvent;
import com.udla.markenx.api.classroom.students.application.ports.out.StudentCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterStudentCommandHandler implements RegisterStudentUseCase {

    private final ValidateTermUseCase validateTermUseCase;
    private final QueryCourseUseCase queryCourseUseCase;
    private final StudentCommandRepository repository;
    private final ApplicationEventPublisher events;

    private void ensureCourseTermIsUpcoming(@NonNull RegisterStudentCommand command) {
        var query = new CourseIdQuery(command.courseId());
        CoursePortDTO course = queryCourseUseCase.getCourseById(query);
        var validationQuery = new IsUpcomingTermQuery(course.termId());
        if (!validateTermUseCase.isUpcoming(validationQuery)) {
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

        Student savedStudent = repository.save(newStudent);

        // Domain event for internal CQRS projections
        events.publishEvent(
                new StudentRegisteredEvent(
                        savedStudent.getId(),
                        command.email(),
                        savedStudent.getFullName(),
                        (int) savedStudent.getCode(),
                        savedStudent.getCourseId(),
                        savedStudent.getLifecycleStatus().name()
                )
        );

        // Integration event for cross-module communication (identity provisioning)
        events.publishEvent(
                new IdentityProvisioningRequestedEvent(
                        savedStudent.getId(),
                        command.email(),
                        savedStudent.getFullName()
                )
        );

        return savedStudent;
    }
}
