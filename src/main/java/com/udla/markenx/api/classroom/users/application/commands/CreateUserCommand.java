package com.udla.markenx.api.classroom.users.application.commands;

public record CreateUserCommand(
    String email,
    String role
) {}