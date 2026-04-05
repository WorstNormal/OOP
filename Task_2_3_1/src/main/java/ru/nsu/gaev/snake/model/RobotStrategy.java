package ru.nsu.gaev.snake.model;

public interface RobotStrategy {
    Direction chooseNextDirection(RobotSnake robot, GameField field);
}
