@ApplicationModule(
        displayName = "Event Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions"
        }
)
package com.udla.markenx.api.game.events;

import org.springframework.modulith.ApplicationModule;