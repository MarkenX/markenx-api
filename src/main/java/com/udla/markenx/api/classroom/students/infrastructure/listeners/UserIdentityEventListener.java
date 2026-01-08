package com.udla.markenx.api.classroom.students.infrastructure.listeners;

import com.udla.markenx.api.classroom.students.application.ports.incoming.UpdateStudentUseCase;
import com.udla.markenx.api.classroom.users.domain.events.UserCreatedEvent;
import com.udla.markenx.api.classroom.users.domain.events.UserCreationFailedEvent;
import com.udla.markenx.api.classroom.users.domain.events.UserDisableFailedEvent;
import com.udla.markenx.api.classroom.users.domain.events.UserDisabledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserIdentityEventListener {

    private final UpdateStudentUseCase useCase;

    @EventListener
    public void on(UserCreatedEvent event) {
        useCase.onUserIdentityCreated(event.studentId(), event.userId());
    }

    @EventListener
    public void on(UserCreationFailedEvent event) {
        useCase.markIdentityCreationFailed(event.studentId());
    }

    @EventListener
    public void on(UserDisabledEvent event) {
        useCase.onUserDisabled(event.studentId());
    }

    @EventListener
    public void on(UserDisableFailedEvent event) {
        useCase.onUserDisableFailed(event.studentId());
    }
}
