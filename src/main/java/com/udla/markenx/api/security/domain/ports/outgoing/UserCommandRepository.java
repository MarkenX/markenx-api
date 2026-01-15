package com.udla.markenx.api.security.domain.ports.outgoing;

import com.udla.markenx.api.security.domain.models.aggregates.User;

public interface UserCommandRepository {
    User save(User user);
    void deleteById(String id);
    void update(User user);
}
