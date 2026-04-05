package ru.nsu.gaev.snake.model;

/**
 * Level record.
 * @param levelNumber level number.
 * @param targetScore target score.
 * @param speed speed.
 */
public record Level(int levelNumber, int targetScore, int speed) {
}
