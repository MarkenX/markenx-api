package com.udla.markenx.api.security.domain.ports.outgoing;

import com.udla.markenx.api.security.domain.models.aggregates.User;

import java.util.Optional;

public interface UserQueryRepository {
    Optional<User> findById(String id);
}
