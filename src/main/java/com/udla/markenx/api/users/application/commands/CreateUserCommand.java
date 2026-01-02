package com.udla.markenx.api.users.application.commands;

public record CreateUserCommand(
    String email,
    String role
) {}