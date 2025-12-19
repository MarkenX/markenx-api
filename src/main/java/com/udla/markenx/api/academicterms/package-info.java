@ApplicationModule(
        displayName = "Term Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
                "shared::infrastructure-jpa",
                "courses::persistence-jpa"
        })
package com.udla.markenx.api.academicterms;

import org.springframework.modulith.ApplicationModule;
