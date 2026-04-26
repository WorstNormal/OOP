package ru.nsu.gaev.snake.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Базовая змейка, содержащая логику движения, роста и столкновений с собой.
 */
public class Snake {
    protected final LinkedList<Point> body;
    protected Direction currentDirection;
    protected Direction nextDirection;
    protected int segmentsToGrow = 0;
    protected boolean alive = true;

    /**
     * Создает змейку.
     *
     * @param startPosition стартовая позиция головы
     * @param startDirection стартовое направление движения
     */
    public Snake(Point startPosition, Direction startDirection) {
        this.body = new LinkedList<>();
        this.body.add(startPosition);
        this.currentDirection = startDirection;
        this.nextDirection = startDirection;
    }

    /**
     * Возвращает тело змейки.
     *
     * @return список точек тела
     */
    public List<Point> getBody() {
        return body;
    }

    /**
     * Возвращает голову змейки.
     *
     * @return координата головы
     */
    public Point getHead() {
        return body.getFirst();
    }

    /**
     * Задает следующее направление движения.
     *
     * @param dir новое направление
     */
    public void setNextDirection(Direction dir) {
        if (body.size() > 1 && dir.isOpposite(currentDirection)) {
            return;
        }
        this.nextDirection = dir;
    }

    /**
     * Возвращает текущее направление.
     *
     * @return текущее направление
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Обрабатывает поедание еды и рост змейки.
     *
     * @param food съеденная еда
     */
    public void eat(Food food) {
        segmentsToGrow += food.type().getGrowthAmount();
    }

    /**
     * Убивает змейку.
     */
    public void kill() {
        this.alive = false;
    }

    /**
     * Проверяет, жива ли змейка.
     *
     * @return true, если змейка жива
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Двигает змейку на один шаг.
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
        } else {
            body.removeLast();
        }
    }

    /**
     * Проверяет, занимает ли змейка указанную точку.
     *
     * @param p проверяемая точка
     * @return true, если точка занята змейкой
     */
    public boolean occupies(Point p) {
        return body.contains(p);
    }

    /**
     * Проверяет столкновение змейки с самой собой.
     *
     * @return true, если обнаружено самостолкновение
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
