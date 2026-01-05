package com.udla.markenx.api.game.scenarios.domain.models.aggregates;

import com.udla.markenx.api.game.scenarios.domain.exceptions.InvalidScenarioTitleException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Scenario {

    private final ScenarioId id;
    private long code;
    private String title;
    private String description;

    public Scenario(
            ScenarioId id,
            String title,
            String description
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // region Getters

    public String getId() {
        return id.value();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    // endregion

    // region Validations

    @Contract("null -> fail")
    public static @NotNull String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidScenarioTitleException();
        }
        return title;
    }

    // endregion
}
