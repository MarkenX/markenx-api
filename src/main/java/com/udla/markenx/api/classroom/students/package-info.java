@ApplicationModule(
        displayName = "Student Management",
        allowedDependencies = {
                "shared::public",
                "security::public",
                "classroom.terms::public",
        })
package com.udla.markenx.api.classroom.students;

import org.springframework.modulith.ApplicationModule;