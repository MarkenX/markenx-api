@ApplicationModule(
        displayName = "Scenario Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
        }
)
package com.udla.markenx.api.game.scenarios;

import org.springframework.modulith.ApplicationModule;