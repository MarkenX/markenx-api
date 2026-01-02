package com.udla.markenx.api.users.application.services;

import com.udla.markenx.api.students.domain.events.StudentRegisteredEvent;
import com.udla.markenx.api.users.application.commands.CreateUserCommand;
import com.udla.markenx.api.users.application.ports.incoming.CreateUserUseCase;
import com.udla.markenx.api.users.application.ports.outgoing.ExternalIdentityPort;
import com.udla.markenx.api.users.domain.events.UserCreatedEvent;
import com.udla.markenx.api.users.domain.events.UserCreationFailedEvent;
import com.udla.markenx.api.users.domain.models.valueobjects.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserIdentityProcess {

    private final CreateUserUseCase createUserUseCase;
    private final ExternalIdentityPort identityPort;
    private final ApplicationEventPublisher events;

    @EventListener
    public void on(StudentRegisteredEvent event) {

        var command = new CreateUserCommand(
                event.email(),
                Role.STUDENT.name()
        );

        String userId = createUserUseCase.handle(command);

        identityPort.createIdentity(event.email())
                .doOnSuccess(email ->
                        events.publishEvent(
                                new UserCreatedEvent(event.studentId(), userId)
                        )
                )
                .doOnError(ex ->
                        events.publishEvent(
                                new UserCreationFailedEvent(event.studentId(), ex.getMessage())
                        )
                )
                .subscribe();
    }
}
