@ApplicationModule(
        displayName = "Scenario Management",
        allowedDependencies = {"shared::aggregates", "shared::valueobjects", "shared::exceptions", "shared::dtos", "game.dimensions::ports-incoming", "game.consumers::ports-incoming", "game.actions::ports-incoming", "game.events::ports-incoming", "game.dimensions :: commands", "game.dimensions :: aggregates", "game.consumers :: commands", "game.consumers :: aggregates", "game.actions :: commands", "game.actions :: aggregates", "game.events :: commands", "game.events :: aggregates", "game.actions :: valueobjects"}
)
package com.udla.markenx.api.game.scenarios;

import org.springframework.modulith.ApplicationModule;