package com.udla.markenx.api.users.domain.ports.outgoing;

import com.udla.markenx.api.users.domain.models.aggregates.User;

public interface UserQueryRepository {
    User findById(String id);
}
