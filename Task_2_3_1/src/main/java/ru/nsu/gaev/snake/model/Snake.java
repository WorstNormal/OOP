package ru.nsu.gaev.snake.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Snake class representing the player's snake in the game.
 */
public class Snake {
    protected final LinkedList<Point> body;
    protected Direction currentDirection;
    protected Direction nextDirection;
    protected int segmentsToGrow = 0;
    protected boolean alive = true;

    /**
     * Constructor for Snake.
     *
     * @param startPosition the starting position of the snake
     * @param startDirection the starting direction of the snake
     */
    public Snake(Point startPosition, Direction startDirection) {
        this.body = new LinkedList<>();
        this.body.add(startPosition);
        this.currentDirection = startDirection;
        this.nextDirection = startDirection;
    }

    /**
     * Get the body of the snake.
     *
     * @return list of points representing the snake body
     */
    public List<Point> getBody() {
        return body;
    }

    /**
     * Get the head of the snake.
     *
     * @return the head point
     */
    public Point getHead() {
        return body.getFirst();
    }

    /**
     * Set the next direction for the snake.
     *
     * @param dir the next direction
     */
    public void setNextDirection(Direction dir) {
        // Cannot reverse direction if length > 1
        if (body.size() > 1 && dir.isOpposite(currentDirection)) {
            return;
        }
        this.nextDirection = dir;
    }

    /**
     * Get the current direction.
     *
     * @return the current direction
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Eat food and grow.
     *
     * @param food the food to eat
     */
    public void eat(Food food) {
        segmentsToGrow += food.type().getGrowthAmount();
    }

    /**
     * Kill the snake.
     */
    public void kill() {
        this.alive = false;
    }

    /**
     * Check if the snake is alive.
     *
     * @return true if alive, false otherwise
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Move the snake one step.
     */
    public void move() {
        if (!alive) {
            return;
        }

        currentDirection = nextDirection;
        Point head = getHead();
        Point newHead = new Point(
                head.x() + currentDirection.getDx(),
                head.y() + currentDirection.getDy()
        );

        body.addFirst(newHead);

        if (segmentsToGrow > 0) {
            segmentsToGrow--;
            // We don't remove the tail because we are growing
        } else {
            body.removeLast(); // Remove tail
        }
    }

    /**
     * Check if the snake occupies a specific point.
     *
     * @param p the point to check
     * @return true if occupied, false otherwise
     */
    public boolean occupies(Point p) {
        return body.contains(p);
    }

    /**
     * Check for self-collision.
     *
     * @return true if self-collision detected, false otherwise
     */
    public boolean checkSelfCollision() {
        if (!alive || body.size() <= 4) {
            return false;
        }
        Point head = getHead();
        for (int i = 1; i < body.size(); i++) {
            if (body.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }
}
