package com.udla.markenx.api.students.domain.models.aggregates;

import com.udla.markenx.api.students.domain.exceptions.InvalidFirstNameException;
import com.udla.markenx.api.students.domain.exceptions.InvalidLastNameException;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

@Getter
public class PersonalInfo {

    private String firstName;
    private String lastName;

    public PersonalInfo(String firstName, String lastName) {
        updateName(firstName, lastName);
    }

    public void updateName(String firstName, String lastName) {
        this.firstName = validateFirstName(firstName);
        this.lastName = validateLastName(lastName);
    }

    @Contract("null -> fail")
    private @NonNull String validateFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new InvalidFirstNameException();
        }
        return firstName;
    }

    @Contract("null -> fail")
    private @NonNull String validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new InvalidLastNameException();
        }
        return lastName;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
