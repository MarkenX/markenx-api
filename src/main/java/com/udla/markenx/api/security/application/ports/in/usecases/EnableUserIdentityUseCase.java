package com.udla.markenx.api.security.application.ports.in.usecases;

import com.udla.markenx.api.shared.domain.events.integration.IdentityEnableRequestedEvent;
import reactor.core.publisher.Mono;

public interface EnableUserIdentityUseCase {
    Mono<Void> handle(IdentityEnableRequestedEvent event);
}
