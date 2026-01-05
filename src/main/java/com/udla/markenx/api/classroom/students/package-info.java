@ApplicationModule(
    displayName = "Student Management",
    allowedDependencies = {
        "shared::aggregates",
        "shared::valueobjects",
        "shared::exceptions",
        "shared::dtos",
        "classroom.users::events"
    }
)
package com.udla.markenx.api.classroom.students;

import org.springframework.modulith.ApplicationModule;