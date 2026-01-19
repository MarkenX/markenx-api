package com.udla.markenx.api.game.attempts.application.ports.in.usecases;

public interface TaskScoreProvider {
    double getMinScoreToPass(String taskId);
}
