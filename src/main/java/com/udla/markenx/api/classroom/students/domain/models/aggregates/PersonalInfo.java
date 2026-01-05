package com.udla.markenx.api.classroom.students.domain.models.aggregates;

import com.udla.markenx.api.classroom.students.domain.models.valueobjects.PersonName;

public class PersonalInfo {

    private PersonName firstName;
    private PersonName lastName;

    public PersonalInfo(String firstName, String lastName) {
        updateName(firstName, lastName);
    }

    public void updateName(String firstName, String lastName) {
        this.firstName = new PersonName(firstName);
        this.lastName = new PersonName(lastName);
    }

    public String getFirstName() {
        return firstName.getValue();
    }

    public String getLastName() {
        return lastName.getValue();
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
