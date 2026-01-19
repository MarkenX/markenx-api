package com.udla.markenx.api.security.application.services;

import com.udla.markenx.api.security.application.commands.CreateUserCommand;
import com.udla.markenx.api.security.application.ports.in.usecases.CreateUserUseCase;
import com.udla.markenx.api.security.application.ports.in.usecases.UserIdentityUseCase;
import com.udla.markenx.api.security.application.ports.out.ExternalIdentityPort;
import com.udla.markenx.api.security.domain.events.UserIdentityRollbackEvent;
import com.udla.markenx.api.security.domain.models.valueobjects.Role;
import com.udla.markenx.api.shared.domain.events.integration.IdentityProvisionedEvent;
import com.udla.markenx.api.shared.domain.events.integration.IdentityProvisioningFailedEvent;
import com.udla.markenx.api.shared.domain.events.integration.IdentityProvisioningRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Service that handles identity provisioning requests.
 * Creates local user records and provisions external identity (Keycloak).
 */
@Component
@RequiredArgsConstructor
public class UserIdentityService implements UserIdentityUseCase {

    private final CreateUserUseCase createUserUseCase;
    private final ExternalIdentityPort identityPort;
    private final ApplicationEventPublisher events;

    @Override
    public Mono<Void> handle(IdentityProvisioningRequestedEvent event) {

        return createUser(event)
                .flatMap(userId ->
                        provisionIdentity(event.email())
                                .thenReturn(userId)
                )
                .doOnSuccess(userId ->
                        events.publishEvent(
                                new IdentityProvisionedEvent(event.sourceEntityId(), userId)
                        )
                )
                .onErrorResume(ex -> rollback(event, ex)).then();
    }

    private @NonNull Mono<String> createUser(IdentityProvisioningRequestedEvent event) {
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
            @NonNull IdentityProvisioningRequestedEvent event,
            @NonNull Throwable ex) {

        events.publishEvent(
                new UserIdentityRollbackEvent(
                        event.email(),
                        ex.getMessage()
                )
        );

        events.publishEvent(
                new IdentityProvisioningFailedEvent(
                        event.sourceEntityId(),
                        ex.getMessage()
                )
        );

        return Mono.empty();
    }

}
