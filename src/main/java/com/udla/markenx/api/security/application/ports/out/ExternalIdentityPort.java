package com.udla.markenx.api.security.application.ports.out;

import reactor.core.publisher.Mono;

public interface ExternalIdentityPort {
    Mono<String> createIdentity(String email);
    Mono<Void> disableIdentity(String email);
    Mono<Void> enableIdentity(String email);
}
