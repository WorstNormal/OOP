package ru.nsu.gaev.snake.model.entity;

import ru.nsu.gaev.snake.model.common.FoodType;
import ru.nsu.gaev.snake.model.common.Point;

/**
 * Еда на игровом поле.
 *
 * @param position координата еды
 * @param type тип еды
 */
public record Food(Point position, FoodType type) implements GameObject {
}
