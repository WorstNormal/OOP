package ru.nsu.gaev.snake.model.core;

/**
 * Слушатель событий модели игры.
 */
public interface ModelListener {
    /**
     * Вызывается при каждом игровом тике.
     *
     * @param snapshot снимок состояния поля
     */
    void onTick(FieldSnapshot snapshot);

    /**
     * Вызывается при столкновении.
     *
     * @param snapshot снимок состояния поля
     */
    void onCollision(FieldSnapshot snapshot);
}

