package com.udla.markenx.api.security.infrastructure.listeners;

import com.udla.markenx.api.security.application.ports.in.UserIdentityUseCase;
import com.udla.markenx.api.shared.domain.events.integration.IdentityProvisioningRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens to identity provisioning requests from other modules
 * (e.g., students module) and triggers user identity creation.
 */
@Component
@RequiredArgsConstructor
public class IdentityProvisioningRequestedListener {

    private final UserIdentityUseCase userIdentityUseCase;

    @EventListener
    public void on(IdentityProvisioningRequestedEvent event) {
        userIdentityUseCase
                .handle(event)
                .subscribe();
    }
}
