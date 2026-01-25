package com.udla.markenx.api.security.infrastructure.persistence.jdbc;

import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import com.udla.markenx.api.security.domain.models.aggregates.User;
import com.udla.markenx.api.security.application.ports.out.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación JDBC del repositorio de comandos para User.
 * El método save() implementa semántica UPSERT: inserta si no existe, actualiza si existe.
 */
@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserCommandRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Guarda un usuario usando semántica UPSERT.
     * Verifica existencia por ID y decide si insertar o actualizar.
     *
     * @param user la entidad a persistir
     * @return la entidad persistida
     */
    @Override
    @Transactional
    public User save(@NonNull User user) {
        if (existsById(user.getId())) {
            update(user);
        } else {
            insert(user);
        }
        return user;
    }

    @Override
    public void deleteById(String id) {
        jdbcTemplate.update("""
            UPDATE users
            SET lifecycle_status = ?
            WHERE id = ?
            """,
                LifecycleStatus.DISABLED.name(),
                id
        );
    }

    private boolean existsById(String id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    private void insert(@NonNull User user) {
        jdbcTemplate.update("""
            INSERT INTO users
            (id, lifecycle_status, email)
            VALUES (?, ?, ?)
            """,
                user.getId(),
                user.getLifecycleStatus().name(),
                user.getEmail()
        );
    }

    private void update(@NonNull User user) {
        jdbcTemplate.update("""
            UPDATE users
            SET lifecycle_status = ?, email = ?
            WHERE id = ?
            """,
                user.getLifecycleStatus().name(),
                user.getEmail(),
                user.getId()
        );
    }
}
