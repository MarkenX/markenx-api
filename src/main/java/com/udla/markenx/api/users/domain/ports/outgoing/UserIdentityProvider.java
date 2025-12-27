package com.udla.markenx.api.users.domain.ports.outgoing;

public interface UserIdentityProvider {
    void provisionIdentity(String email);
}
