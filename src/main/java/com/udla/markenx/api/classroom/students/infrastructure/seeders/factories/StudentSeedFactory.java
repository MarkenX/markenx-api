package com.udla.markenx.api.classroom.students.infrastructure.seeders.factories;

import com.udla.markenx.api.classroom.students.infrastructure.seeders.valueobjects.StudentSeedDefinition;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.List;

public final class StudentSeedFactory {

    private StudentSeedFactory() {}

    @Contract(" -> new")
    public static @NonNull List<StudentSeedDefinition> defaultStudents() {
        return List.of(
                new StudentSeedDefinition("Christian", "Jácome", "christian.jacome@udla.edu.ec"),
                new StudentSeedDefinition("Ana", "Rodríguez", "ana.rodriguez@udla.edu.ec"),
                new StudentSeedDefinition("Luis", "García", "luis.garcia@udla.edu.ec"),
                new StudentSeedDefinition("Sofía", "Martínez", "sofia.martinez@udla.edu.ec")
        );
    }
}
