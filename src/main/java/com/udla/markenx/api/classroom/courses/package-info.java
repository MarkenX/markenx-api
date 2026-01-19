@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::public",
                "classroom.terms::public",
                "classroom.assignments::ports-incoming"
        })
package com.udla.markenx.api.classroom.courses;

import org.springframework.modulith.ApplicationModule;