package com.udla.markenx.api.classroom.users.domain.ports.outgoing;

import com.udla.markenx.api.classroom.users.domain.models.aggregates.User;

public interface UserCommandRepository {
    User save(User user);
    void deleteById(String id);
}
