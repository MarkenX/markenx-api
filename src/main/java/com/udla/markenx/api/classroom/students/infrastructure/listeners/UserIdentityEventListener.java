package com.udla.markenx.api.classroom.students.infrastructure.listeners;

import com.udla.markenx.api.classroom.students.application.ports.in.usecases.UpdateStudentUseCase;
import com.udla.markenx.api.shared.domain.events.integration.IdentityDisabledEvent;
import com.udla.markenx.api.shared.domain.events.integration.IdentityEnabledEvent;
import com.udla.markenx.api.shared.domain.events.integration.IdentityProvisionedEvent;
import com.udla.markenx.api.shared.domain.events.integration.IdentityProvisioningFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserIdentityEventListener {

    private final UpdateStudentUseCase useCase;

    @EventListener
    public void on(IdentityProvisionedEvent event) {
        useCase.onUserIdentityCreated(event.sourceEntityId(), event.identityId());
    }

    @EventListener
    public void on(IdentityProvisioningFailedEvent event) {
        useCase.markIdentityCreationFailed(event.sourceEntityId());
    }

    @EventListener
    public void on(IdentityDisabledEvent event) {
        useCase.onUserDisabled(event.sourceEntityId());
    }

    @EventListener
    public void on(IdentityEnabledEvent event) {
        useCase.onUserEnabled(event.sourceEntityId());
    }
}
