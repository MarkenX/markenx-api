package com.udla.markenx.api.security.application.ports.outgoing;

import reactor.core.publisher.Mono;

public interface ExternalIdentityPort {
    Mono<String> createIdentity(String email);
    Mono<Void> disableIdentity(String email);
}
