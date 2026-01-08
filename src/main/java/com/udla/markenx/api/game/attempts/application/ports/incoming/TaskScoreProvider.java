package com.udla.markenx.api.game.attempts.application.ports.incoming;

public interface TaskScoreProvider {
    double getMinScoreToPass(String taskId);
}
