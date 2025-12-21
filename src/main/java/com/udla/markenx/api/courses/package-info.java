@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
        }
)
package com.udla.markenx.api.courses;

import org.springframework.modulith.ApplicationModule;