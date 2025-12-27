package com.udla.markenx.api.users.domain.ports.outgoing;

public interface UserIdentityProvider {
    String provisionIdentity(String email);
}
