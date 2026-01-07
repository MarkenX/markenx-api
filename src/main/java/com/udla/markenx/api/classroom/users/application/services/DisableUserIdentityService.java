package com.udla.markenx.api.classroom.users.application.services;

import com.udla.markenx.api.classroom.students.domain.events.StudentDisableRequestedEvent;
import com.udla.markenx.api.classroom.users.application.ports.incoming.DisableUserIdentityUseCase;
import com.udla.markenx.api.classroom.users.application.ports.outgoing.ExternalIdentityPort;
import com.udla.markenx.api.classroom.users.domain.events.UserDisableFailedEvent;
import com.udla.markenx.api.classroom.users.domain.events.UserDisabledEvent;
import com.udla.markenx.api.classroom.users.domain.models.aggregates.User;
import com.udla.markenx.api.classroom.users.domain.ports.outgoing.UserCommandRepository;
import com.udla.markenx.api.classroom.users.domain.ports.outgoing.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DisableUserIdentityService implements DisableUserIdentityUseCase {

    private final ExternalIdentityPort identityPort;
    private final UserQueryRepository userQueryRepository;
    private final UserCommandRepository userCommandRepository;
    private final ApplicationEventPublisher events;

    @Override
    public Mono<Void> handle(StudentDisableRequestedEvent event) {
        return disableLocalUser(event.userId())
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

    private void publishSuccessEvent(@NonNull StudentDisableRequestedEvent event) {
        events.publishEvent(new UserDisabledEvent(event.studentId(), event.userId()));
    }

    private @NonNull Mono<Void> rollback(
            @NonNull StudentDisableRequestedEvent event,
            @NonNull Throwable ex) {

        userQueryRepository.findById(event.userId()).ifPresent(user -> {
            user.enable();
            userCommandRepository.update(user);
        });

        events.publishEvent(new UserDisableFailedEvent(event.studentId(), ex.getMessage()));

        return Mono.empty();
    }
}
