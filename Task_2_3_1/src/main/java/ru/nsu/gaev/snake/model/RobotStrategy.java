package ru.nsu.gaev.snake.model;

/**
 * RobotStrategy interface.
 */
public interface RobotStrategy {
    Direction chooseNextDirection(RobotSnake robot, GameField field);
}
