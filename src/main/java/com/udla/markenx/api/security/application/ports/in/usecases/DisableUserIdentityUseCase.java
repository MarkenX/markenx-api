package com.udla.markenx.api.security.application.ports.in.usecases;

import com.udla.markenx.api.shared.domain.events.integration.IdentityDisableRequestedEvent;
import reactor.core.publisher.Mono;

public interface DisableUserIdentityUseCase {
    Mono<Void> handle(IdentityDisableRequestedEvent event);
}
