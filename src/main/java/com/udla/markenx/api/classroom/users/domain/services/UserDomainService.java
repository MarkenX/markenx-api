package com.udla.markenx.api.classroom.users.domain.services;

import com.udla.markenx.api.classroom.users.domain.exceptions.EmailAlreadyExistsException;
import com.udla.markenx.api.classroom.users.domain.models.aggregates.User;
import org.jspecify.annotations.NonNull;

import java.util.Collection;

public class UserDomainService {

    /**
     * Ensures that the email address of the given user is unique within the provided collection of users.
     * If a user with the same email address already exists in the collection, an exception is thrown.
     *
     * @param users the collection of existing users whose email addresses must be checked for uniqueness
     * @param user the user whose email address is being validated for uniqueness
     * @throws EmailAlreadyExistsException if the email address of the given user is already in use
     */
    public static void ensureEmailIsUnique(@NonNull Collection<User> users, User user) {
        users.forEach(s -> {
            if (s.getEmail().equals(user.getEmail())) {
                throw new EmailAlreadyExistsException(user.getEmail());
            }
        });
    }
}
