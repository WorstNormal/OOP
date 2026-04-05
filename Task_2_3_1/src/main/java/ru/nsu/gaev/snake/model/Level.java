package ru.nsu.gaev.snake.model;

/**
 * Level record.
 *
 * @param levelNumber level number.
 * @param targetScore target score.
 * @param tickDurationNs tick duration in nanoseconds.
 */
public record Level(int levelNumber, int targetScore, long tickDurationNs) {
}
