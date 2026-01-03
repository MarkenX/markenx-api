package com.udla.markenx.api.users.infrastructure.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EmailConfig {

    private final Set<String> allowedDomains;

    public EmailConfig(
            @Value("${email.allowed-domains}") String domains
    ) {
        this.allowedDomains = Set.of(domains.toLowerCase().split(","));
    }

    public Set<String> allowedDomains() {
        return allowedDomains;
    }
}