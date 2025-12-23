package com.udla.markenx.api.students.domain.models.valueobjects;

import com.udla.markenx.api.students.domain.exceptions.InvalidPersonNameFormatException;
import com.udla.markenx.api.students.domain.exceptions.PersonNameCannotBeEmptyException;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public final class PersonName {

    private static final Pattern VALID_NAME_PATTERN =
            Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+$");

    private String value;

    public PersonName(String value) {
        change(value);
    }

    public void change(String value) {
        this.value = normalize(validate(value));
    }

    @Contract("null -> fail")
    private @NonNull String validate(String value) {
        if (value == null || value.isBlank()) {
            throw new PersonNameCannotBeEmptyException();
        }
        if (!VALID_NAME_PATTERN.matcher(value.trim()).matches()) {
            throw new InvalidPersonNameFormatException();
        }
        return value;
    }

    private @NonNull String normalize(@NonNull String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonName that)) return false;
        return value.equalsIgnoreCase(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.toLowerCase());
    }

    @Override
    public String toString() {
        return value;
    }
}
