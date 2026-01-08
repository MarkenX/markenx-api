package com.udla.markenx.api.game.actions.domain.models.aggregates;

import com.udla.markenx.api.game.actions.domain.exceptions.*;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionCategory;
import com.udla.markenx.api.game.actions.domain.models.valueobjects.ActionEffect;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Action {

    private final ActionId id;
    private final String name;
    private final String description;
    private final BigDecimal cost;
    private final ActionCategory category;
    private final boolean isInitiallyLocked;
    private final String prerequisiteActionId;
    private final List<ActionEffect> effects;

    // region Constructors

    public Action(
            ActionId id,
            String name,
            String description,
            BigDecimal cost,
            ActionCategory category,
            boolean isInitiallyLocked,
            String prerequisiteActionId,
            List<ActionEffect> effects
    ) {
        this.id = id;
        validateName(name);
        validateDescription(description);
        validateCost(cost);
        validateCategory(category);

        this.name = name;
        this.description = description;
        this.cost = cost;
        this.category = category;
        this.isInitiallyLocked = isInitiallyLocked;
        this.prerequisiteActionId = prerequisiteActionId;
        this.effects = effects != null ? new ArrayList<>(effects) : new ArrayList<>();
    }

    public Action(
            String id,
            String name,
            String description,
            BigDecimal cost,
            ActionCategory category,
            boolean isInitiallyLocked,
            String prerequisiteActionId,
            List<ActionEffect> effects
    ) {
        this(new ActionId(id), name, description, cost, category, isInitiallyLocked, prerequisiteActionId, effects);
    }

    // endregion

    // region Factories

    public static @NonNull Action create(
            String name,
            String description,
            BigDecimal cost,
            ActionCategory category,
            boolean isInitiallyLocked,
            String prerequisiteActionId,
            List<ActionEffect> effects
    ) {
        var id = ActionId.generate();
        return new Action(
                id,
                name,
                description,
                cost,
                category,
                isInitiallyLocked,
                prerequisiteActionId,
                effects
        );
    }

    public static @NonNull Action createWithId(
            String id,
            String name,
            String description,
            BigDecimal cost,
            ActionCategory category,
            boolean isInitiallyLocked,
            String prerequisiteActionId,
            List<ActionEffect> effects
    ) {
        return new Action(
                new ActionId(id),
                name,
                description,
                cost,
                category,
                isInitiallyLocked,
                prerequisiteActionId,
                effects
        );
    }

    // endregion

    // region Getters

    public ActionId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public ActionCategory getCategory() {
        return category;
    }

    public boolean isInitiallyLocked() {
        return isInitiallyLocked;
    }

    public String getPrerequisiteActionId() {
        return prerequisiteActionId;
    }

    public List<ActionEffect> getEffects() {
        return Collections.unmodifiableList(effects);
    }

    // endregion

    // region Validations

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidActionNameException();
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidActionDescriptionException();
        }
    }

    private void validateCost(BigDecimal cost) {
        if (cost == null || cost.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidActionCostException();
        }
    }

    private void validateCategory(ActionCategory category) {
        if (category == null) {
            throw new InvalidActionCategoryException();
        }
    }

    // endregion

    // region Equals & HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // endregion
}
