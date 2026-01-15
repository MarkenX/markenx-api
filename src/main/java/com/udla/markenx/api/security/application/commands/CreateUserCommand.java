package com.udla.markenx.api.security.application.commands;

public record CreateUserCommand(
    String email,
    String role
) {}