package com.udla.markenx.api.game.scenarios.domain.models.aggregates;

import com.udla.markenx.api.game.scenarios.domain.exceptions.InvalidScenarioDescriptionException;
import com.udla.markenx.api.game.scenarios.domain.exceptions.InvalidScenarioTitleException;

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
        this.title = validateTitle(title);
        this.description = validateDescription(description);
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

    public String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidScenarioTitleException();
        }
        return title;
    }

    public String validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidScenarioDescriptionException();
        }
        return description;
    }

    // endregion
}
