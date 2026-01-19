@ApplicationModule(
        displayName = "Student Management",
        allowedDependencies = {
                "shared::public",
                "security::public",
                "game.attempts::public",
                "classroom.terms::public",
        })
package com.udla.markenx.api.classroom.students;

import org.springframework.modulith.ApplicationModule;