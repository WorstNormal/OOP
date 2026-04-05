package ru.nsu.gaev.snake.model;

public record Food(Point position, FoodType type) implements GameObject {
}
