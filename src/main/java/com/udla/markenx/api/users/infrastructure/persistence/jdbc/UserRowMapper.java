package com.udla.markenx.api.users.infrastructure.persistence.jdbc;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import com.udla.markenx.api.users.domain.models.aggregates.User;
import com.udla.markenx.api.users.domain.models.valueobjects.Email;
import com.udla.markenx.api.users.domain.models.valueobjects.Role;
import com.udla.markenx.api.users.infrastructure.email.EmailConfig;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class UserRowMapper implements RowMapper<User> {

    private final EmailConfig emailConfig;

    @Override
    public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getString("id"),
                LifecycleStatus.valueOf(rs.getString("lifecycle_status")),
                Email.of(rs.getString("email"), emailConfig.allowedDomains()),
                Role.valueOf(rs.getString("role"))
        );
    }
}
