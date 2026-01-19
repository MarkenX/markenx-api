package com.udla.markenx.api.game.attempts.application.ports.in;

public interface TaskScoreProvider {
    double getMinScoreToPass(String taskId);
}
