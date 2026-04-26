package ru.nsu.gaev.snake.model.core;

import java.util.List;
import ru.nsu.gaev.snake.model.common.Point;

/**
 * Снимок состояния игрового поля только для чтения.
 * Используется для предоставления AI ограниченного доступа к ин��ормации о поле.
 */
public interface FieldSnapshot {
    /**
     * Получает ширину поля.
     *
     * @return ширина в ячейках
     */
    int getWidth();

    /**
     * Получает высоту поля.
     *
     * @return высота в ячейках
     */
    int getHeight();

    /**
     * Получает неизменяемый список позиций препятствий.
     *
     * @return список точек препятствий
     */
    List<Point> getObstacles();

    /**
     * Получает неизменяемый список позиций еды.
     *
     * @return список точек еды
     */
    List<Point> getFoods();

    /**
     * Проверяет, свободна ли точка на поле.
     *
     * @param point проверяемая точка
     * @return true, если клетка свободна
     */
    boolean isPointFree(Point point);

    /**
     * Получает текущую позицию робота.
     * Используется для определения собственной позиции при выборе хода.
     *
     * @return позиция робота
     */
    Point getMyPosition();
}

