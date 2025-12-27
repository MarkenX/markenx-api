package com.udla.markenx.api.users.domain.models.aggregates;

import com.udla.markenx.api.users.domain.models.valueobjects.Email;

public class User {

    private Email email;

    public void updateEmail(Email email) {
        this.email = email;
    }

    public Email getEmail() {
        return this.email;
    }
}
