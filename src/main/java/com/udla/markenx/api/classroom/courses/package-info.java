@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
                "in",
                "classroom.assignments::ports-incoming",
                "classroom.terms::public"
        }
)
package com.udla.markenx.api.classroom.courses;

import org.springframework.modulith.ApplicationModule;