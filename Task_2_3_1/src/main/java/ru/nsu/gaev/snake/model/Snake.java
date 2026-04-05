package ru.nsu.gaev.snake.model;

import java.util.LinkedList;
import java.util.List;

public class Snake {
    protected final LinkedList<Point> body;
    protected Direction currentDirection;
    protected Direction nextDirection;
    protected int segmentsToGrow = 0;
    protected boolean alive = true;

    public Snake(Point startPosition, Direction startDirection) {
        this.body = new LinkedList<>();
        this.body.add(startPosition);
        this.currentDirection = startDirection;
        this.nextDirection = startDirection;
    }

    public List<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public void setNextDirection(Direction dir) {
        // Cannot reverse direction if length > 1
        if (body.size() > 1 && dir.isOpposite(currentDirection)) {
            return;
        }
        this.nextDirection = dir;
    }
    
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void eat(Food food) {
        segmentsToGrow += food.getType().getGrowthAmount();
    }
    
    public void kill() {
        this.alive = false;
    }
    
    public boolean isAlive() {
        return alive;
    }

    public void move() {
        if (!alive) return;

        currentDirection = nextDirection;
        Point head = getHead();
        Point newHead = new Point(head.x() + currentDirection.getDx(), head.y() + currentDirection.getDy());

        body.addFirst(newHead);

        if (segmentsToGrow > 0) {
            segmentsToGrow--;
            // We don't remove the tail because we are growing
        } else {
            body.removeLast(); // Remove tail
        }
    }
    
    public boolean occupies(Point p) {
        return body.contains(p);
    }
    
    public boolean checkSelfCollision() {
        if (!alive || body.size() <= 4) return false;
        Point head = getHead();
        for (int i = 1; i < body.size(); i++) {
            if (body.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }
}
