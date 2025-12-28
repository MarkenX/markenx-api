@ApplicationModule(
    displayName = "Student Management",
    allowedDependencies = {
        "shared::aggregates",
        "shared::valueobjects",
        "shared::exceptions",
        "shared::dtos",
        "users::commands",
        "users::ports-incoming"
    }
)
package com.udla.markenx.api.students;

import org.springframework.modulith.ApplicationModule;