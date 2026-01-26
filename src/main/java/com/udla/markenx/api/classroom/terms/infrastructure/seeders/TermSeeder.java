package com.udla.markenx.api.classroom.terms.infrastructure.seeders;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.CreateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.CreateTermUseCase;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermQueryRepository;
import com.udla.markenx.api.classroom.terms.domain.models.aggregates.Term;
import com.udla.markenx.api.classroom.terms.infrastructure.seeders.factories.TermSeedFactory;
import com.udla.markenx.api.shared.infrastructure.seeders.BaseSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile("dev")
@Order(1)
@RequiredArgsConstructor
public class TermSeeder extends BaseSeeder implements CommandLineRunner {

    private final CreateTermUseCase createTermUseCase;
    private final TermQueryRepository termQueryRepository;

    @Override
    public String name() {
        return "Terms";
    }

    @Override
    protected void doSeed() {
        seedTermsFor();
    }

    @Override
    public void run(String @NonNull ... args) {
        seed();
    }

    /**
     * Siembra términos solo si no existen.
     * Utiliza año y fechas como clave natural para verificar existencia.
     */
    private void seedTermsFor() {
        Set<String> existingTermKeys = getExistingTermKeys();

        termCommands().stream()
                .filter(command -> !termExists(command, existingTermKeys))
                .forEach(command -> {
                    createTermUseCase.handle(command);
                    log.debug("Term created: year={}, startDate={}", command.year(), command.startDate());
                });
    }

    /**
     * Obtiene las claves (año-startDate-endDate) de los términos existentes.
     */
    private Set<String> getExistingTermKeys() {
        return termQueryRepository.findAll().stream()
                .map(this::toTermKey)
                .collect(Collectors.toSet());
    }

    /**
     * Verifica si un término ya existe usando año y fechas como clave natural.
     */
    private boolean termExists(@NonNull CreateTermCommand command, Set<String> existingKeys) {
        String key = toCommandKey(command);
        boolean exists = existingKeys.contains(key);
        if (exists) {
            log.debug("Term already exists, skipping: year={}, startDate={}", command.year(), command.startDate());
        }
        return exists;
    }

    private @NonNull String toTermKey(@NonNull Term term) {
        return term.getYear() + "-" + term.getStartDate() + "-" + term.getEndDate();
    }

    private @NonNull String toCommandKey(@NonNull CreateTermCommand command) {
        return command.year() + "-" + command.startDate() + "-" + command.endDate();
    }

    @Contract(" -> new")
    private @NonNull List<CreateTermCommand> termCommands() {
        return List.of(
                TermSeedFactory.past(),
                TermSeedFactory.current(),
                TermSeedFactory.future()
        );
    }
}
