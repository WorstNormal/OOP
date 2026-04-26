package ru.nsu.gaev.snake.model.entity;

import ru.nsu.gaev.snake.model.common.Point;

/**
 * Препятствие на поле.
 *
 * @param position координата препятствия
 */
public record Obstacle(Point position) implements GameObject {
}
