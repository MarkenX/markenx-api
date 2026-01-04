@ApplicationModule(
        displayName = "Assignment Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
        }
)
package com.udla.markenx.api.assignments;

import org.springframework.modulith.ApplicationModule;