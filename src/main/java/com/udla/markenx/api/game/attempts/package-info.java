@ApplicationModule(
        displayName = "Attempt Management",
        allowedDependencies = {
                "shared::public",
                "classroom.assignments::public",
                "classroom.students::public",
        }
)
package com.udla.markenx.api.game.attempts;

import org.springframework.modulith.ApplicationModule;