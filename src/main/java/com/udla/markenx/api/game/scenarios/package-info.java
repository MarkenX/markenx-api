@ApplicationModule(
        displayName = "Scenario Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
                "shared::dtos",
                "game.dimensions::ports-incoming",
                "game.consumers::ports-incoming",
                "game.actions::ports-incoming",
                "game.events::ports-incoming"
        }
)
package com.udla.markenx.api.game.scenarios;

import org.springframework.modulith.ApplicationModule;