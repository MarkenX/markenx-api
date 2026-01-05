package com.udla.markenx.api.users.application.services;

import com.udla.markenx.api.classroom.students.domain.events.StudentRegisteredEvent;
import com.udla.markenx.api.users.application.commands.CreateUserCommand;
import com.udla.markenx.api.users.application.ports.incoming.CreateUserUseCase;
import com.udla.markenx.api.users.application.ports.incoming.UserIdentityUseCase;
import com.udla.markenx.api.users.application.ports.outgoing.ExternalIdentityPort;
import com.udla.markenx.api.users.domain.events.UserCreatedEvent;
import com.udla.markenx.api.users.domain.events.UserCreationFailedEvent;
import com.udla.markenx.api.users.domain.events.UserIdentityRollbackEvent;
import com.udla.markenx.api.users.domain.models.valueobjects.Role;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserIdentityService implements UserIdentityUseCase {

    private final CreateUserUseCase createUserUseCase;
    private final ExternalIdentityPort identityPort;
    private final ApplicationEventPublisher events;

    @Override
    public Mono<Void> handle(StudentRegisteredEvent event) {

        return createUser(event)
                .flatMap(userId ->
                        provisionIdentity(event.email())
                                .thenReturn(userId)
                )
                .doOnSuccess(userId ->
                        events.publishEvent(
                                new UserCreatedEvent(event.studentId(), userId)
                        )
                )
                .onErrorResume(ex -> rollback(event, ex)).then();
    }

    private @NonNull Mono<String> createUser(StudentRegisteredEvent event) {
        return Mono.fromCallable(() ->
                createUserUseCase.handle(
                        new CreateUserCommand(
                                event.email(),
                                Role.STUDENT.name()
                        )
                )
        );
    }

    private @NonNull Mono<Void> provisionIdentity(String email) {
        return identityPort.createIdentity(email).then();
    }

    private @NonNull Mono<? extends String> rollback(
            @NonNull StudentRegisteredEvent event,
            @NonNull Throwable ex) {

        events.publishEvent(
                new UserIdentityRollbackEvent(
                        event.email(),
                        ex.getMessage()
                )
        );

        events.publishEvent(
                new UserCreationFailedEvent(
                        event.studentId(),
                        ex.getMessage()
                )
        );

        return Mono.empty();
    }

}
