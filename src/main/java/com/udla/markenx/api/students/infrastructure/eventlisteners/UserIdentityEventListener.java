package com.udla.markenx.api.students.infrastructure.eventlisteners;

import com.udla.markenx.api.students.application.ports.incoming.UpdateStudentUseCase;
import com.udla.markenx.api.users.domain.events.UserCreatedEvent;
import com.udla.markenx.api.users.domain.events.UserCreationFailedEvent;
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
}
