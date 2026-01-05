@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
                "classroom.students::ports-incoming",
                "classroom.assignments::ports-incoming"
        }
)
package com.udla.markenx.api.classroom.courses;

import org.springframework.modulith.ApplicationModule;