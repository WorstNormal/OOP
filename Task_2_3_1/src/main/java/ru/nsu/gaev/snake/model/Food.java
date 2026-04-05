package ru.nsu.gaev.snake.model;

public class Food implements GameObject {
    private final Point position;
    private final FoodType type;

    public Food(Point position, FoodType type) {
        this.position = position;
        this.type = type;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    public FoodType getType() {
        return type;
    }
}
