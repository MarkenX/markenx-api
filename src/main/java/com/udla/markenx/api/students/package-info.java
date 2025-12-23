@ApplicationModule(
    displayName = "Student Management",
    allowedDependencies = {
        "shared::aggregates",
        "shared::valueobjects",
        "shared::exceptions",
        "shared::dtos",
    }
)
package com.udla.markenx.api.students;

import org.springframework.modulith.ApplicationModule;