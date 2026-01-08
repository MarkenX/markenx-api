@ApplicationModule(
        displayName = "Dimension Management",
        allowedDependencies = {
                "shared::aggregates",
                "shared::valueobjects",
                "shared::exceptions",
        })
package com.udla.markenx.api.game.dimensions;

import org.springframework.modulith.ApplicationModule;