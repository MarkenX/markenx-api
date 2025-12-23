package com.udla.markenx.api.students.domain.models.valueobjects;

import com.udla.markenx.api.students.domain.exceptions.EmailCannotBeEmptyException;
import com.udla.markenx.api.students.domain.exceptions.EmailDomainNotAllowedException;
import com.udla.markenx.api.students.domain.exceptions.InvalidEmailFormatException;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

@Getter
public final class Email {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    @Contract("_, _ -> new")
    public static @NonNull Email of(String value, Set<String> allowedDomains) {
        return new Email(normalize(validate(value, allowedDomains)));
    }

    @Contract("_ -> new")
    public static @NonNull Email of(String value) {
        return new Email(normalize(value));
    }

    @Contract("null, _ -> fail")
    private static @NonNull String validate(String value, Set<String> allowedDomains) {
        if (value == null || value.isBlank()) {
            throw new EmailCannotBeEmptyException();
        }

        if (!EMAIL_PATTERN.matcher(value.trim()).matches()) {
            throw new InvalidEmailFormatException();
        }

        String domain = extractDomain(value);

        if (!allowedDomains.contains(domain)) {
            throw new EmailDomainNotAllowedException(domain);
        }

        return value;
    }

    private static @NonNull String extractDomain(@NonNull String email) {
        return email.substring(email.indexOf("@") + 1).toLowerCase();
    }

    private static @NonNull String normalize(@NonNull String email) {
        return email.trim().toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email email)) return false;
        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
