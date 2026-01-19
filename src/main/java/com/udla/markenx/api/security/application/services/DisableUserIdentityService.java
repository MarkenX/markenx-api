package com.udla.markenx.api.security.application.services;

import com.udla.markenx.api.security.application.ports.in.DisableUserIdentityUseCase;
import com.udla.markenx.api.security.application.ports.out.ExternalIdentityPort;
import com.udla.markenx.api.security.domain.ports.outgoing.UserCommandRepository;
import com.udla.markenx.api.security.domain.ports.outgoing.UserQueryRepository;
import com.udla.markenx.api.shared.domain.events.integration.IdentityDisableFailedEvent;
import com.udla.markenx.api.shared.domain.events.integration.IdentityDisableRequestedEvent;
import com.udla.markenx.api.shared.domain.events.integration.IdentityDisabledEvent;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service that handles identity disable requests.
 * Disables local user records and external identity (Keycloak).
 */
@Service
@RequiredArgsConstructor
public class DisableUserIdentityService implements DisableUserIdentityUseCase {

    private final ExternalIdentityPort identityPort;
    private final UserQueryRepository userQueryRepository;
    private final UserCommandRepository userCommandRepository;
    private final ApplicationEventPublisher events;

    @Override
    public Mono<Void> handle(IdentityDisableRequestedEvent event) {
        return disableLocalUser(event.identityId())
                .then(disableExternalIdentity(event.email()))
                .doOnSuccess(v -> publishSuccessEvent(event))
                .onErrorResume(ex -> rollback(event, ex))
                .then();
    }

    private @NonNull Mono<Void> disableLocalUser(String userId) {
        return Mono.fromRunnable(() -> {
            userQueryRepository.findById(userId).ifPresent(user -> {
                user.disable();
                userCommandRepository.update(user);
            });
        });
    }

    private @NonNull Mono<Void> disableExternalIdentity(String email) {
        return identityPort.disableIdentity(email);
    }

    private void publishSuccessEvent(@NonNull IdentityDisableRequestedEvent event) {
        events.publishEvent(new IdentityDisabledEvent(event.sourceEntityId()));
    }

    private @NonNull Mono<Void> rollback(
            @NonNull IdentityDisableRequestedEvent event,
            @NonNull Throwable ex) {

        userQueryRepository.findById(event.identityId()).ifPresent(user -> {
            user.enable();
            userCommandRepository.update(user);
        });

        events.publishEvent(new IdentityDisableFailedEvent(event.sourceEntityId(), ex.getMessage()));

        return Mono.empty();
    }
}
