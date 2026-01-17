@ApplicationModule(
        displayName = "Term Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
        })
package com.udla.markenx.api.classroom.terms;

import org.springframework.modulith.ApplicationModule;
