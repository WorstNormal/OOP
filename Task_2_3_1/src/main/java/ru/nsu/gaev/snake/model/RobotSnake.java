package ru.nsu.gaev.snake.model;

import java.util.Random;

/**
 * RobotSnake class.
 */
public class RobotSnake extends Snake {
    private static final Random RANDOM = new Random();
    private final RobotStrategy strategy;

    /**
     * RobotSnake constructor.
     * @param start start position.
     * @param startDirection start direction.
     * @param strategy strategy.
     */
    public RobotSnake(Point start, Direction startDirection, RobotStrategy strategy) {
        super(start, startDirection);
        this.strategy = strategy;
    }

    public void determineNextMove(GameField field) {
        Direction nextDir = strategy.chooseNextDirection(this, field);
        if (nextDir != null) {
            setNextDirection(nextDir);
        }
    }
}
