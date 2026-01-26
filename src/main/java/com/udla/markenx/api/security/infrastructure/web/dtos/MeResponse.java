package com.udla.markenx.api.security.infrastructure.web.dtos;

import java.util.List;

public record MeResponse(
        String username,
        String email,
        String fullName,
        List<String> roles
) {}
