@ApplicationModule(
    displayName = "Student Management",
    allowedDependencies = {
        "shared::aggregates",
        "shared::valueobjects",
        "shared::exceptions",
    }
)
package com.udla.markenx.api.students;

import org.springframework.modulith.ApplicationModule;