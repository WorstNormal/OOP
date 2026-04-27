package ru.nsu.gaev.snake.model.core;

import java.util.List;
import ru.nsu.gaev.snake.model.common.Level;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.entity.Food;
import ru.nsu.gaev.snake.model.entity.Obstacle;
import ru.nsu.gaev.snake.model.entity.RobotSnake;
import ru.nsu.gaev.snake.model.entity.Snake;

/**
 * Неизменяемый по смыслу обзор игрового поля для рендеринга и стратегий.
 */
public interface GameFieldView {
    int getWidth();

    int getHeight();

    Snake getPlayer();

    List<RobotSnake> getRobots();

    List<Food> getFoods();

    List<Obstacle> getObstacles();

    Level getCurrentLevel();

    int getScore();

    boolean isGameOver();

    boolean isGameWon();

    boolean isGameDraw();

    boolean isStarted();

    boolean isPointFree(Point point);
}