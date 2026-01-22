package com.udla.markenx.api.shared.infrastructure.seeders;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseSeeder implements Seeder {

    @Override
    public final void seed() {
        long startMs = System.currentTimeMillis();
        log.info("[SEED] Starting {}", name());

        try {
            doSeed();
            long tookMs = System.currentTimeMillis() - startMs;
            log.info("[SEED] Completed {} in {} ms", name(), tookMs);
        } catch (Exception e) {
            log.error("[SEED] Failed {}", name(), e);
            throw e;
        }
    }

    protected abstract void doSeed();
}
