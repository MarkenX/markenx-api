package com.udla.markenx.api.classroom.students.application.services;

import com.udla.markenx.api.classroom.students.application.commands.DisableStudentCommand;
import com.udla.markenx.api.classroom.students.application.commands.UpdateStudentCommand;
import com.udla.markenx.api.classroom.students.application.ports.incoming.UpdateStudentUseCase;
import com.udla.markenx.api.classroom.students.application.queries.GetStudentByIdQuery;
import com.udla.markenx.api.classroom.students.domain.events.StudentDisableRequestedEvent;
import com.udla.markenx.api.classroom.students.domain.events.StudentIdentityActivatedEvent;
import com.udla.markenx.api.classroom.students.domain.events.StudentIdentityFailedEvent;
import com.udla.markenx.api.classroom.students.domain.exceptions.StudentAlreadyDisabledException;
import com.udla.markenx.api.classroom.students.domain.exceptions.StudentNotActiveException;
import com.udla.markenx.api.classroom.students.domain.models.aggregates.Student;
import com.udla.markenx.api.classroom.students.domain.models.valueobjects.StudentStatus;
import com.udla.markenx.api.classroom.students.domain.ports.outgoing.StudentCommandRepository;
import com.udla.markenx.api.classroom.users.domain.models.aggregates.User;
import com.udla.markenx.api.classroom.users.domain.ports.outgoing.UserQueryRepository;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateStudentService implements UpdateStudentUseCase {

    private final StudentCommandRepository repository;
    private final UserQueryRepository userQueryRepository;
    private final ApplicationEventPublisher events;

    @Override
    public Student getById(GetStudentByIdQuery query) {
        return repository.findById(query.id());
    }

    @Override
    public void markIdentityCreated(String studentId) {
        Student student = repository.findById(studentId);
        student.markIdentityCreated();
        repository.save(student);

        events.publishEvent(
                new StudentIdentityActivatedEvent(studentId)
        );
    }

    @Override
    public void markIdentityCreationFailed(String studentId) {
        Student student = repository.findById(studentId);
        student.markIdentityCreationFailed();
        student.disable();
        repository.save(student);

        events.publishEvent(
                new StudentIdentityFailedEvent(studentId)
        );
    }

    @Override
    public void onUserIdentityCreated(String studentId, String userId) {
        Student student = repository.findById(studentId);

        student.assignUser(userId);
        student.markIdentityCreated();

        repository.update(student);
    }

    @Override
    public void disable(@NonNull DisableStudentCommand command) {
        Student student = repository.findById(command.studentId());

        validateCanDisable(student);

        String email = getUserEmail(student.getUserId());

        events.publishEvent(
                new StudentDisableRequestedEvent(
                        student.getId(),
                        student.getUserId(),
                        email
                )
        );
    }

    @Override
    public Student update(@NonNull UpdateStudentCommand command) {
        Student student = repository.findById(command.studentId());

        student.update(command.firstName(), command.lastName());
        student.changeCourse(command.courseId());

        repository.update(student);
        return student;
    }

    @Override
    public void onUserDisabled(String studentId) {
        Student student = repository.findById(studentId);
        student.disable();
        repository.update(student);
    }

    @Override
    public void onUserDisableFailed(String studentId) {
        // El rollback ya se realizó en el servicio de users
        // Aquí solo podríamos registrar el fallo o notificar
    }

    private void validateCanDisable(@NonNull Student student) {
        if (student.getLifecycleStatus() == LifecycleStatus.DISABLED) {
            throw new StudentAlreadyDisabledException(student.getId());
        }
        if (!StudentStatus.ACTIVE.name().equals(student.getStatusCode())) {
            throw new StudentNotActiveException(student.getId());
        }
    }

    private String getUserEmail(String userId) {
        return userQueryRepository.findById(userId)
                .map(User::getEmail)
                .orElseThrow(() -> new IllegalStateException(
                        "User not found for userId: " + userId));
    }
}
