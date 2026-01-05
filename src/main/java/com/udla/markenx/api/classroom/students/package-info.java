@ApplicationModule(
    displayName = "Student Management",
    allowedDependencies = {
        "shared::aggregates",
        "shared::valueobjects",
        "shared::exceptions",
        "shared::dtos",
        "users::events"
    }
)
package com.udla.markenx.api.classroom.students;

import org.springframework.modulith.ApplicationModule;