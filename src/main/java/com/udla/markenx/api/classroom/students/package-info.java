@ApplicationModule(
        displayName = "Student Management",
        allowedDependencies = {
                "shared::public",
                "security::public",
                "game.attempts::public",
                "classroom.terms::public",
                "classroom.courses::public"
        })
package com.udla.markenx.api.classroom.students;

import org.springframework.modulith.ApplicationModule;