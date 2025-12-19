@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::infrastructure-jpa",
                "academicterms::persistence-jpa"
        }
)
package com.udla.markenx.api.courses;

import org.springframework.modulith.ApplicationModule;