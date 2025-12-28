package com.udla.markenx.api.users.infrastructure.persistence.jdbc;

import com.udla.markenx.api.users.domain.models.aggregates.User;
import com.udla.markenx.api.users.domain.ports.outgoing.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserCommandRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper mapper;

    @Override
    public User save(@NonNull User user) {
        jdbcTemplate.update("""
            INSERT INTO users
            (id, lifecycle_status, email)
            VALUES (?, ?, ?)
            """,
                user.getId(),
                user.getLifecycleStatus(),
                user.getEmail()
        );
        return user;
    }

    @Override
    public User findById(String id) {
        return null;
    }
}
