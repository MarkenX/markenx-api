package com.udla.markenx.api.security.application.ports.out;

import com.udla.markenx.api.security.domain.models.aggregates.User;

public interface UserCommandRepository {
    User save(User user);
    void deleteById(String id);
    void update(User user);
}
