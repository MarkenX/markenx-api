package com.udla.markenx.api.classroom.users.application.ports.outgoing;

import reactor.core.publisher.Mono;

public interface ExternalIdentityPort {
    Mono<String> createIdentity(String email);
}
