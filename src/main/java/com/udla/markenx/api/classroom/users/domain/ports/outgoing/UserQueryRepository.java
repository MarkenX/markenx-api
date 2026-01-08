package com.udla.markenx.api.classroom.users.domain.ports.outgoing;

import com.udla.markenx.api.classroom.users.domain.models.aggregates.User;

import java.util.Optional;

public interface UserQueryRepository {
    Optional<User> findById(String id);
}
