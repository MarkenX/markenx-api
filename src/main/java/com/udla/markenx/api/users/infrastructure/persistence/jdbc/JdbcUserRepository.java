package com.udla.markenx.api.users.infrastructure.persistence.jdbc;

import com.udla.markenx.api.users.domain.models.aggregates.User;
import com.udla.markenx.api.users.domain.ports.outgoing.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserCommandRepository {

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User findById(String id) {
        return null;
    }
}
