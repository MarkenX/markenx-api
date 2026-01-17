@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
                "classroom.students::ports-incoming",
                "classroom.assignments::ports-incoming",
                "classroom.academicterms::public"
        }
)
package com.udla.markenx.api.classroom.courses;

import org.springframework.modulith.ApplicationModule;