package com.udla.markenx.api.users.domain.ports.outgoing;

import com.udla.markenx.api.users.domain.models.aggregates.User;

public interface UserCommandRepository {
    User save(User user);
    User findById(String id);
}
