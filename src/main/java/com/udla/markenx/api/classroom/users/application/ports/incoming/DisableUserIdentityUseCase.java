package com.udla.markenx.api.classroom.users.application.ports.incoming;

import com.udla.markenx.api.classroom.students.domain.events.StudentDisableRequestedEvent;
import reactor.core.publisher.Mono;

public interface DisableUserIdentityUseCase {
    Mono<Void> handle(StudentDisableRequestedEvent event);
}
