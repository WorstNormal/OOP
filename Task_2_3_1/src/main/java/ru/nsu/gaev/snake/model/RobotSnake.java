package ru.nsu.gaev.snake.model;

import java.util.Random;

/**
 * Змейка, управляемая стратегией искусственного интеллекта.
 */
public class RobotSnake extends Snake {
    private static final Random RANDOM = new Random();
    private final RobotStrategy strategy;

    /**
     * Создает робота-змейку.
     *
     * @param start стартовая позиция
     * @param startDirection стартовое направление
     * @param strategy стратегия выбора хода
     */
    public RobotSnake(Point start, Direction startDirection, RobotStrategy strategy) {
        super(start, startDirection);
        this.strategy = strategy;
    }

    /**
     * Определяет следующее направление движения.
     *
     * @param field игровое поле
     */
    public void determineNextMove(GameField field) {
        Direction nextDir = strategy.chooseNextDirection(this, field);
        if (nextDir != null) {
            setNextDirection(nextDir);
        }
    }
}
