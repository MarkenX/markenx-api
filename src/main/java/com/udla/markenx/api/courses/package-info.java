@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::aggregates",
        }
)
package com.udla.markenx.api.courses;

import org.springframework.modulith.ApplicationModule;