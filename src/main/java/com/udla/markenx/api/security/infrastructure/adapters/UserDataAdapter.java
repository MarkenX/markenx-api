package com.udla.markenx.api.security.infrastructure.adapters;

import com.udla.markenx.api.classroom.students.application.ports.out.UserDataPort;
import com.udla.markenx.api.security.domain.models.aggregates.User;
import com.udla.markenx.api.security.application.ports.out.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter that implements the UserDataPort from the students module.
 * This acts as an Anti-Corruption Layer, exposing only the data
 * that the students module needs without coupling to internal security types.
 */
@Component
@RequiredArgsConstructor
public class UserDataAdapter implements UserDataPort {

    private final UserQueryRepository userQueryRepository;

    @Override
    public Optional<String> findEmailByUserId(String userId) {
        return userQueryRepository.findById(userId)
                .map(User::getEmail);
    }
}
