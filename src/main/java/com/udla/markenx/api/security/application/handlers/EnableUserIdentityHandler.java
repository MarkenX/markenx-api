package com.udla.markenx.api.security.application.handlers;

import com.udla.markenx.api.security.application.ports.in.usecases.EnableUserIdentityUseCase;
import com.udla.markenx.api.security.application.ports.out.ExternalIdentityPort;
import com.udla.markenx.api.security.application.ports.out.UserCommandRepository;
import com.udla.markenx.api.security.application.ports.out.UserQueryRepository;
import com.udla.markenx.api.shared.domain.events.integration.IdentityEnableFailedEvent;
import com.udla.markenx.api.shared.domain.events.integration.IdentityEnableRequestedEvent;
import com.udla.markenx.api.shared.domain.events.integration.IdentityEnabledEvent;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service that handles identity enable requests.
 * Enables local user records and external identity (Keycloak).
 */
@Service
@RequiredArgsConstructor
public class EnableUserIdentityHandler implements EnableUserIdentityUseCase {

    private final ExternalIdentityPort identityPort;
    private final UserQueryRepository userQueryRepository;
    private final UserCommandRepository userCommandRepository;
    private final ApplicationEventPublisher events;

    @Override
    public Mono<Void> handle(IdentityEnableRequestedEvent event) {
        return enableLocalUser(event.identityId())
                .then(enableExternalIdentity(event.email()))
                .doOnSuccess(v -> publishSuccessEvent(event))
                .onErrorResume(ex -> rollback(event, ex))
                .then();
    }

    private @NonNull Mono<Void> enableLocalUser(String userId) {
        return Mono.fromRunnable(() -> {
            userQueryRepository.findById(userId).ifPresent(user -> {
                user.enable();
                userCommandRepository.save(user);
            });
        });
    }

    private @NonNull Mono<Void> enableExternalIdentity(String email) {
        return identityPort.enableIdentity(email);
    }

    private void publishSuccessEvent(@NonNull IdentityEnableRequestedEvent event) {
        events.publishEvent(new IdentityEnabledEvent(event.sourceEntityId()));
    }

    private @NonNull Mono<Void> rollback(
            @NonNull IdentityEnableRequestedEvent event,
            @NonNull Throwable ex) {

        userQueryRepository.findById(event.identityId()).ifPresent(user -> {
            user.disable();
            userCommandRepository.save(user);
        });

        events.publishEvent(new IdentityEnableFailedEvent(event.sourceEntityId(), ex.getMessage()));

        return Mono.empty();
    }
}
