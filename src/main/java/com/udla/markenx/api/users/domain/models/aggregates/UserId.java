package com.udla.markenx.api.users.domain.models.aggregates;

import com.udla.markenx.api.shared.domain.models.aggregates.Identifier;

public class UserId extends Identifier {
    protected UserId(String value) {
        super(value);
    }

    public static UserId generate() {
        return new UserId(java.util.UUID.randomUUID().toString());
    }
}
