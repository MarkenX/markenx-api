package com.udla.markenx.api.students.application.projections;

import com.udla.markenx.api.students.domain.ports.outgoing.StudentCommandRepository;
import com.udla.markenx.api.users.domain.events.UserCreatedEvent;
import com.udla.markenx.api.users.domain.events.UserCreationFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentIdentityProjection {

    private final StudentCommandRepository repository;

    @EventListener
    public void on(UserCreatedEvent event) {
        repository.assignUser(event.studentId(), event.userId());
    }

    @EventListener
    public void on(UserCreationFailedEvent event) {
        repository.markIdentityFailed(event.studentId());
    }
}
