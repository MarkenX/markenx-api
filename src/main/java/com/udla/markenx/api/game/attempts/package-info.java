@ApplicationModule(
        displayName = "Attempts Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
        }
)
package com.udla.markenx.api.game.attempts;

import org.springframework.modulith.ApplicationModule;