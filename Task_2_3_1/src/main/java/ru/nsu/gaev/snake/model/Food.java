package ru.nsu.gaev.snake.model;

/**
 * Food record.
 *
 * @param position position.
 * @param type type.
 */
public record Food(Point position, FoodType type) implements GameObject {
}
