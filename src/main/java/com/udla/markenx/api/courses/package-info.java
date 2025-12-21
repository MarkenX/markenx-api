@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::infrastructure-jpa",
                "academicterms::ports-incoming",
        }
)
package com.udla.markenx.api.courses;

import org.springframework.modulith.ApplicationModule;