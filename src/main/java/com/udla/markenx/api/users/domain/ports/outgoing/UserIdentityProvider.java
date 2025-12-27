package com.udla.markenx.api.users.domain.ports.outgoing;

public interface UserIdentityProvider {
    void createExternalIdentity(String email);
}
