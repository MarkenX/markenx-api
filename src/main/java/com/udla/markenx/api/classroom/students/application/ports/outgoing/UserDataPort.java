package com.udla.markenx.api.classroom.students.application.ports.outgoing;

import java.util.Optional;

/**
 * Anti-Corruption Layer port for accessing user identity data.
 * This port decouples the students module from the security module,
 * following DDD bounded context principles.
 */
public interface UserDataPort {

    /**
     * Finds the email associated with a user identity.
     *
     * @param userId the user identity ID
     * @return the email if found, empty otherwise
     */
    Optional<String> findEmailByUserId(String userId);
}
