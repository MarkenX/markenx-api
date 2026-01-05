package com.udla.markenx.api.classroom.users.application.ports.incoming;

import com.udla.markenx.api.classroom.students.domain.events.StudentRegisteredEvent;
import reactor.core.publisher.Mono;

public interface UserIdentityUseCase {
    Mono<Void> handle(StudentRegisteredEvent event);
}
