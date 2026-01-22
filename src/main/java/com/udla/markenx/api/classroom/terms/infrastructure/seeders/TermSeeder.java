package com.udla.markenx.api.classroom.terms.infrastructure.seeders;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.CreateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.usecases.CreateTermUseCase;
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

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@Order(1)
@RequiredArgsConstructor
public class TermSeeder extends BaseSeeder implements CommandLineRunner {

    private final CreateTermUseCase createTermUseCase;

    @Override
    public String name() {
        return "Terms";
    }

    @Override
    protected void doSeed() {
        seedTermsFor(today());
    }

    @Override
    public void run(String @NonNull ... args) {
        seed();
    }

    private void seedTermsFor(LocalDate referenceDate) {
        termCommands(referenceDate)
                .forEach(createTermUseCase::handle);
    }

    @Contract("_ -> new")
    private @NonNull List<CreateTermCommand> termCommands(LocalDate referenceDate) {
        return List.of(
                TermSeedFactory.past(referenceDate),
                TermSeedFactory.current(referenceDate),
                TermSeedFactory.future(referenceDate)
        );
    }

    @Contract(" -> new")
    private @NonNull LocalDate today() {
        return LocalDate.now();
    }
}
