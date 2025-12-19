@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::infrastructure-jpa",
                "academicterms::aggregates",
                "academicterms::ports-incoming",
                "academicterms::queries",
        }
)
package com.udla.markenx.api.courses;

import org.springframework.modulith.ApplicationModule;