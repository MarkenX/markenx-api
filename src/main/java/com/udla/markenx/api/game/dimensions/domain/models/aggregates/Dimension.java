package com.udla.markenx.api.game.dimensions.domain.models.aggregates;

public class Dimension {

    private final DimensionId id;
    private final String name;
    private final String description;
    private final double consumerExpectation;
    private final double productInitialOffer;

    public Dimension(
            DimensionId id,
            String name,
            String description,
            double consumerExpectation,
            double productInitialOffer
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.consumerExpectation = consumerExpectation;
        this.productInitialOffer = productInitialOffer;
    }

    // region Getters

    public DimensionId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getConsumerExpectation() {
        return consumerExpectation;
    }

    public double getProductInitialOffer() {
        return productInitialOffer;
    }

    // endregion
}
