@ApplicationModule(
    displayName = "Student Management",
    allowedDependencies = {
        "shared::aggregates",
    }
)
package com.udla.markenx.api.students;

import org.springframework.modulith.ApplicationModule;