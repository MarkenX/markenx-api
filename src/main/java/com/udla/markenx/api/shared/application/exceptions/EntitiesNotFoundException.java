package com.udla.markenx.api.shared.application.exceptions;

import com.udla.markenx.api.shared.domain.exceptions.EntityException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("LombokGetterMayBeUsed")
public class EntitiesNotFoundException extends EntityException {

    private final String entityName;
    private final String criteria;
    private final Object criteriaValue;

    public EntitiesNotFoundException(String code, String entityName, @Nullable String criteria, @Nullable Object criteriaValue) {
        super(code, buildMessage(entityName, criteria, criteriaValue));
        this.entityName = entityName;
        this.criteria = criteria;
        this.criteriaValue = criteriaValue;
    }

    public EntitiesNotFoundException(String code, String entityName, Map<String, Object> criteria) {
        super(code, buildMessage(entityName, criteria));
        this.entityName = entityName;
        this.criteria = "multiple";
        this.criteriaValue = criteria;
    }

    public EntitiesNotFoundException(String code, String entityName) {
        super(code, buildMessage(entityName, null, null));
        this.entityName = entityName;
        this.criteria = null;
        this.criteriaValue = null;
    }

    @Contract(pure = true)
    private static @NonNull String buildMessage(String entityName, @Nullable String criteria, @Nullable Object value) {
        if (criteria == null || value == null) {
            return String.format("No se encontraron registros de %s", entityName);
        }

        String formattedValue = formatValue(value);
        return String.format("No se encontraron registros de %s con %s: %s", entityName, criteria, formattedValue);
    }

    private static @NonNull String buildMessage(String entityName, @NonNull Map<String, Object> criteria) {
        String criteriaStr = criteria.entrySet().stream()
                .map(e -> e.getKey() + "=" + formatValue(e.getValue()))
                .collect(Collectors.joining(", "));
        return String.format("No se encontraron registros de %s con criterios: %s", entityName, criteriaStr);
    }

    @Contract(pure = true)
    private static @NonNull String formatValue(@NonNull Object value) {
        // Manejar colecciones
        if (value instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                return "[]";
            }
            int maxDisplay = 5;
            if (collection.size() > maxDisplay) {
                String preview = collection.stream()
                        .limit(maxDisplay)
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                return String.format("[%s, ... %d más]", preview, collection.size() - maxDisplay);
            }
            return collection.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", ", "[", "]"));
        }

        // Manejar arrays
        if (value.getClass().isArray()) {
            return formatArrayValue(value);
        }

        // Valor simple
        return value.toString();
    }

    @Contract(pure = true)
    private static @NonNull String formatArrayValue(@NonNull Object array) {
        int length = Array.getLength(array);
        if (length == 0) {
            return "[]";
        }

        int maxDisplay = 5;
        if (length > maxDisplay) {
            List<String> preview = new ArrayList<>(maxDisplay);
            for (int i = 0; i < maxDisplay; i++) {
                preview.add(String.valueOf(Array.get(array, i)));
            }
            return String.format("[%s, ... %d más]", String.join(", ", preview), length - maxDisplay);
        }

        List<String> elements = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            elements.add(String.valueOf(Array.get(array, i)));
        }
        return elements.stream().collect(Collectors.joining(", ", "[", "]"));
    }

    public String getEntityName() {
        return entityName;
    }

    public String getCriteria() {
        return criteria;
    }

    public Object getCriteriaValue() {
        return criteriaValue;
    }

    public static @NonNull EntitiesNotFoundException none(String code, String entityName) {
        return new EntitiesNotFoundException(code, entityName);
    }

    public static @NonNull EntitiesNotFoundException byCriteria(String code, String entityName, String criteria, Object value) {
        return new EntitiesNotFoundException(code, entityName, criteria, value);
    }

    public static @NonNull EntitiesNotFoundException byMultipleCriteria(String code, String entityName, Map<String, Object> criteria) {
        return new EntitiesNotFoundException(code, entityName, criteria);
    }
}