package com.udla.markenx.api.security.infrastructure.listeners;

import com.udla.markenx.api.security.application.ports.incoming.DisableUserIdentityUseCase;
import com.udla.markenx.api.shared.domain.events.integration.IdentityDisableRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens to identity disable requests from other modules
 * (e.g., students module) and triggers user identity disabling.
 */
@Component
@RequiredArgsConstructor
public class IdentityDisableRequestedListener {

    private final DisableUserIdentityUseCase disableUserIdentityUseCase;

    @EventListener
    public void on(IdentityDisableRequestedEvent event) {
        disableUserIdentityUseCase
                .handle(event)
                .subscribe();
    }
}
