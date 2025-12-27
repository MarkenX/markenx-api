package com.udla.markenx.api.users.infrastructure.persistence.jdbc;

import com.udla.markenx.api.users.domain.models.aggregates.User;
import com.udla.markenx.api.users.domain.ports.outgoing.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserQueryRepository {

    @Override
    public User findById(String id) {
        return null;
    }
}
