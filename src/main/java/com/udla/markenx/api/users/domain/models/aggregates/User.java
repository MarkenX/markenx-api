package com.udla.markenx.api.users.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import com.udla.markenx.api.users.domain.models.valueobjects.Email;
import com.udla.markenx.api.users.domain.models.valueobjects.Role;

@SuppressWarnings("LombokGetterMayBeUsed")
public class User extends Entity {

    private final UserId id;
    private Email email;
    private final Role role;

    private User(String id, LifecycleStatus status, Email email, Role role) {
        super(status);
        this.id = new UserId(id);
        this.email = email;
        this.role = role;
    }

    public void updateEmail(Email email) {
        this.email = email;
    }

    public String getId() {
        return this.id.value();
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return this.email.getValue();
    }
}
