package com.udla.markenx.api.auth.infrastructure.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDTO(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("expires_in") int expiresIn,
    @JsonProperty("refresh_token") String refreshToken,
    @JsonProperty("token_type") String tokenType
) {}