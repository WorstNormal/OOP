package ru.nsu.gaev.snake.model.entity;

import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.core.FieldSnapshot;
import ru.nsu.gaev.snake.model.strategy.RobotStrategy;

/**
 * Змейка, управляемая стратегией искусственного интеллекта.
 */
public class RobotSnake extends Snake {
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
     * @param field снимок игрового поля с ограниченным доступом
     */
    public void determineNextMove(FieldSnapshot field) {
        Direction nextDir = strategy.chooseNextDirection(this, field);
        if (nextDir != null) {
            setNextDirection(nextDir);
        }
    }
}
