package ru.nsu.gaev.snake.model;

/**
 * Препятствие на поле.
 *
 * @param position координата препятствия
 */
public record Obstacle(Point position) implements GameObject {
}
