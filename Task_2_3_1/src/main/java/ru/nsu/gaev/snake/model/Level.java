package ru.nsu.gaev.snake.model;

public class Level {
    private final int levelNumber;
    private final int targetScore;
    private final long tickDurationMs;

    public Level(int levelNumber, int targetScore, long tickDurationMs) {
        this.levelNumber = levelNumber;
        this.targetScore = targetScore;
        this.tickDurationMs = tickDurationMs;
    }

    public int getLevelNumber() { return levelNumber; }
    public int getTargetScore() { return targetScore; }
    public long getTickDurationMs() { return tickDurationMs; }
}
