package ru.nsu.gaev.snake.model;

/**
 * Еда на игровом поле.
 *
 * @param position координата еды
 * @param type тип еды
 */
public record Food(Point position, FoodType type) implements GameObject {
}
