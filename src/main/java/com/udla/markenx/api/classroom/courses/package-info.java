@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
                "students::ports-incoming",
                "assignments::ports-incoming"
        }
)
package com.udla.markenx.api.classroom.courses;

import org.springframework.modulith.ApplicationModule;