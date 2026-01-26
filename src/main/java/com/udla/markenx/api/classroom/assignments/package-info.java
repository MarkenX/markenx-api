@ApplicationModule(
        displayName = "Assignment Management",
        allowedDependencies = {
                "shared::public",
                "classroom.courses::public",
        })
package com.udla.markenx.api.classroom.assignments;

import org.springframework.modulith.ApplicationModule;