package ru.nsu.gaev.snake.model;

import java.util.Random;

public class RobotSnake extends Snake {
    private static final Random RANDOM = new Random();
    private final RobotStrategy strategy;

    public RobotSnake(Point startPosition, Direction startDirection, RobotStrategy strategy) {
        super(startPosition, startDirection);
        this.strategy = strategy;
    }

    public void determineNextMove(GameField field) {
        Direction nextDir = strategy.chooseNextDirection(this, field);
        if (nextDir != null) {
            setNextDirection(nextDir);
        }
    }
}
