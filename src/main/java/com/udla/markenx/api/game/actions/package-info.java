@ApplicationModule(
        displayName = "Action Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions"
        }
)
package com.udla.markenx.api.game.actions;

import org.springframework.modulith.ApplicationModule;