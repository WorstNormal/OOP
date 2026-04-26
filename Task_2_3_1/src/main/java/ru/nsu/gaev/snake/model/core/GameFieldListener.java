package ru.nsu.gaev.snake.model.core;

/**
 * Слушатель изменений игрового поля.
 */
@FunctionalInterface
public interface GameFieldListener {
    void onGameFieldChanged(GameFieldView field);
}