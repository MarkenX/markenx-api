package com.udla.markenx.api.students.application.services;

import com.udla.markenx.api.students.application.ports.incoming.UpdateStudentUseCase;
import com.udla.markenx.api.students.application.queries.GetStudentByIdQuery;
import com.udla.markenx.api.students.domain.events.StudentIdentityActivatedEvent;
import com.udla.markenx.api.students.domain.events.StudentIdentityFailedEvent;
import com.udla.markenx.api.students.domain.models.aggregates.Student;
import com.udla.markenx.api.students.domain.ports.outgoing.StudentCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateStudentService implements UpdateStudentUseCase {

    private final StudentCommandRepository repository;
    private final ApplicationEventPublisher events;

    @Override
    public Student getById(GetStudentByIdQuery query) {
        return null;
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
}
