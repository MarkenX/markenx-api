package com.udla.markenx.api.classroom.users.infrastructure.listeners;

import com.udla.markenx.api.classroom.students.domain.events.StudentRegisteredEvent;
import com.udla.markenx.api.classroom.users.application.ports.incoming.UserIdentityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentRegisteredListener {

    private final UserIdentityUseCase userIdentityUseCase;

    @EventListener
    public void on(StudentRegisteredEvent event) {
        userIdentityUseCase
                .handle(event)
                .subscribe();
    }
}
