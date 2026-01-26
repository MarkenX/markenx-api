package com.udla.markenx.api.security.application.ports.in.usecases;

import com.udla.markenx.api.shared.domain.events.integration.IdentityProvisioningRequestedEvent;
import reactor.core.publisher.Mono;

/**
 * Use case for handling identity provisioning requests.
 * Creates user identity in response to integration events.
 */
public interface UserIdentityUseCase {
    Mono<Void> handle(IdentityProvisioningRequestedEvent event);
}
