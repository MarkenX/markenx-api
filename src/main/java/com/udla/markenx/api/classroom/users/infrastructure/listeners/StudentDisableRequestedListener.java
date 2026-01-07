package com.udla.markenx.api.classroom.users.infrastructure.listeners;

import com.udla.markenx.api.classroom.students.domain.events.StudentDisableRequestedEvent;
import com.udla.markenx.api.classroom.users.application.ports.incoming.DisableUserIdentityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentDisableRequestedListener {

    private final DisableUserIdentityUseCase disableUserIdentityUseCase;

    @EventListener
    public void on(StudentDisableRequestedEvent event) {
        disableUserIdentityUseCase
                .handle(event)
                .subscribe();
    }
}
