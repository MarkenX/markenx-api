package com.udla.markenx.api.users.application.commands;

import com.udla.markenx.api.users.domain.models.valueobjects.Role;

public record CreateUserCommand(
    String email,
    String role
) {}