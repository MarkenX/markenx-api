package com.udla.markenx.api.security.application.ports.in.usecases;

import com.udla.markenx.api.shared.domain.events.integration.IdentityDisableRequestedEvent;
import reactor.core.publisher.Mono;

/**
 * Use case for handling identity disable requests.
 * Disables user identity in response to integration events.
 */
public interface DisableUserIdentityUseCase {
    Mono<Void> handle(IdentityDisableRequestedEvent event);
}
