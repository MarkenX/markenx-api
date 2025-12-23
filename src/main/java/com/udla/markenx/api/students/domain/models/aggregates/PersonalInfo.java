package com.udla.markenx.api.students.domain.models.aggregates;

import com.udla.markenx.api.students.domain.models.valueobjects.Email;
import com.udla.markenx.api.students.domain.models.valueobjects.PersonName;

@SuppressWarnings("LombokGetterMayBeUsed")
public class PersonalInfo {

    private PersonName firstName;
    private PersonName lastName;
    private Email email;

    public PersonalInfo(String firstName, String lastName, Email email) {
        updateName(firstName, lastName);
        updateEmail(email);
    }

    public void updateName(String firstName, String lastName) {
        this.firstName = new PersonName(firstName);
        this.lastName = new PersonName(lastName);
    }

    public void updateEmail(Email email) {
        this.email = email;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public Email getEmail() {
        return this.email;
    }
}
