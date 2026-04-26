package ru.nsu.gaev.snake.model.strategy;

import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.core.FieldSnapshot;
import ru.nsu.gaev.snake.model.entity.RobotSnake;

/**
 * Стратегия выбора следующего хода для робота.
 */
public interface RobotStrategy {
    Direction chooseNextDirection(RobotSnake robot, FieldSnapshot field);
}
