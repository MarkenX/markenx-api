package com.udla.markenx.api.security.application.ports.in.commands;

public record CreateUserCommand(
    String email,
    String role
) {}