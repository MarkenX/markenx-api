package com.udla.markenx.api.game.events.domain.models.aggregates;

import com.udla.markenx.api.game.events.domain.exceptions.*;
import com.udla.markenx.api.game.events.domain.models.valueobjects.EventEffect;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("LombokGetterMayBeUsed")
public class GameEvent {

    private final GameEventId id;
    private final String title;
    private final String description;
    private final List<EventEffect> effects;

    // region Constructors

    public GameEvent(
            GameEventId id,
            String title,
            String description,
            List<EventEffect> effects
    ) {
        this.id = id;
        validateTitle(title);
        validateDescription(description);

        this.title = title;
        this.description = description;
        this.effects = effects != null ? new ArrayList<>(effects) : new ArrayList<>();
    }

    public GameEvent(
            String id,
            String title,
            String description,
            List<EventEffect> effects
    ) {
        this(new GameEventId(id), title, description, effects);
    }

    // endregion

    // region Factories

    public static @NonNull GameEvent create(
            String title,
            String description,
            List<EventEffect> effects
    ) {
        var id = GameEventId.generate();
        return new GameEvent(
                id,
                title,
                description,
                effects
        );
    }

    // endregion

    // region Getters

    public GameEventId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<EventEffect> getEffects() {
        return Collections.unmodifiableList(effects);
    }

    // endregion

    // region Validations

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidEventTitleException();
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidEventDescriptionException();
        }
    }

    // endregion

    // region Equals & HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameEvent that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // endregion
}
