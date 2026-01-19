@ApplicationModule(
        displayName = "Course Management",
        allowedDependencies = {
                "shared::public",
                "classroom.terms::public",
                "in"
        })
package com.udla.markenx.api.classroom.courses;

import org.springframework.modulith.ApplicationModule;