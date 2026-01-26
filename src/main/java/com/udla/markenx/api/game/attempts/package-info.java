@ApplicationModule(
        displayName = "Attempt Management",
        allowedDependencies = {
                "shared::public",
                "classroom.assignments::public"
        }
)
package com.udla.markenx.api.game.attempts;

import org.springframework.modulith.ApplicationModule;