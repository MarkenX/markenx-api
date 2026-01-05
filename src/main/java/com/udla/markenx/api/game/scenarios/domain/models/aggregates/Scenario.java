package com.udla.markenx.api.game.scenarios.domain.models.aggregates;

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
}
