@ApplicationModule(
        displayName = "Consumer Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions"
        }
)
package com.udla.markenx.api.game.consumers;

import org.springframework.modulith.ApplicationModule;