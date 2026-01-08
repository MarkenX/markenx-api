package com.udla.markenx.api.classroom.users.infrastructure.persistence.jooq;

import com.udla.markenx.api.classroom.users.domain.models.aggregates.User;
import com.udla.markenx.api.classroom.users.domain.ports.outgoing.UserQueryRepository;
import com.udla.markenx.api.classroom.users.infrastructure.persistence.jdbc.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JooqUserRepository implements UserQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper;

    @Override
    public Optional<User> findById(@NonNull String id) {
        var results = jdbcTemplate.query("""
            SELECT *
            FROM users
            WHERE id = ?
            """,
                rowMapper,
                id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }
}
