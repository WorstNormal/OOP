package ru.nsu.gaev.snake.model.entity;

import ru.nsu.gaev.snake.model.common.Point;

/**
 * Базовый контракт для игровых объектов, которые имеют координату.
 */
public interface GameObject {
    Point position();
}
