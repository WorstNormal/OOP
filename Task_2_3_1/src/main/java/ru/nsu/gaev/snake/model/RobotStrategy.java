package ru.nsu.gaev.snake.model;

/**
 * Стратегия выбора следующего хода для робота.
 */
public interface RobotStrategy {
    Direction chooseNextDirection(RobotSnake robot, GameField field);
}
