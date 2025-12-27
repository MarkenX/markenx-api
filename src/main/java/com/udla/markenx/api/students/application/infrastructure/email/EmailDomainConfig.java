package com.udla.markenx.api.students.application.infrastructure.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EmailDomainConfig {

    private final Set<String> allowedDomains;

    public EmailDomainConfig(
            @Value("${email.allowed-domains}") String domains
    ) {
        this.allowedDomains = Set.of(domains.toLowerCase().split(","));
    }

    public Set<String> allowedDomains() {
        return allowedDomains;
    }
}