package com.udla.markenx.api.security.infrastructure.listeners;

import com.udla.markenx.api.security.application.ports.in.usecases.EnableUserIdentityUseCase;
import com.udla.markenx.api.shared.domain.events.integration.IdentityEnableRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens to identity enable requests from other modules
 * (e.g., students module) and triggers user identity enabling.
 */
@Component
@RequiredArgsConstructor
public class IdentityEnableRequestedListener {

    private final EnableUserIdentityUseCase enableUserIdentityUseCase;

    @EventListener
    public void on(IdentityEnableRequestedEvent event) {
        enableUserIdentityUseCase
                .handle(event)
                .subscribe();
    }
}
