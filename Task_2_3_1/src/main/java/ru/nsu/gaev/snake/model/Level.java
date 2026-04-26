package ru.nsu.gaev.snake.model;

/**
 * Параметры уровня игры.
 *
 * @param levelNumber номер уровня
 * @param targetScore целевое количество очков
 * @param tickDurationNs длительность тика в наносекундах
 */
public record Level(int levelNumber, int targetScore, long tickDurationNs) {
}
