package com.udla.markenx.api.security.application.ports.out;

import com.udla.markenx.api.security.domain.models.aggregates.User;

import java.util.Optional;

public interface UserQueryRepository {
    Optional<User> findById(String id);
}
