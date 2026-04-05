package ru.nsu.gaev.snake.model;

public class Obstacle implements GameObject {
    private final Point position;
    
    public Obstacle(Point position) {
        this.position = position;
    }
    
    @Override
    public Point getPosition() {
        return position;
    }
}
