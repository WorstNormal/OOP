package ru.nsu.gaev.snake.model.strategy;

import java.util.LinkedList;
import java.util.Queue;
import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.core.FieldSnapshot;
import ru.nsu.gaev.snake.model.entity.RobotSnake;

/**
 * Жадная стратегия, которая ведет робота к ближайшей еде.
 */
public class GreedyStrategy implements RobotStrategy {

    @Override
    public Direction chooseNextDirection(RobotSnake robot, FieldSnapshot field) {
        Point head = robot.getHead();

        Point nearestFood = null;
        int minDistance = Integer.MAX_VALUE;
        for (Point p : field.getFoods()) {
            int dist = Math.abs(p.x() - head.x()) + Math.abs(p.y() - head.y());
            if (dist < minDistance) {
                minDistance = dist;
                nearestFood = p;
            }
        }

        if (nearestFood == null) {
            return fallback(head, robot.getCurrentDirection(), field);
        }

        Direction bestDir = bfs(head, nearestFood, field, robot);
        if (bestDir != null) {
            return bestDir;
        }

        return fallback(head, robot.getCurrentDirection(), field);
    }

    private Direction bfs(Point start, Point target, FieldSnapshot field, RobotSnake robot) {
        boolean[][] visited = new boolean[field.getWidth()][field.getHeight()];
        Queue<Node> queue = new LinkedList<>();

        visited[start.x()][start.y()] = true;

        for (Direction d : Direction.values()) {
            if (robot.getBody().size() > 1 && d.isOpposite(robot.getCurrentDirection())) {
                continue;
            }
            Point next = new Point(start.x() + d.getDx(), start.y() + d.getDy());
            if (isValid(next, field)) {
                queue.add(new Node(next, d));
                visited[next.x()][next.y()] = true;
            }
        }

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            if (curr.point.equals(target)) {
                return curr.firstDir;
            }

            for (Direction d : Direction.values()) {
                Point next = new Point(curr.point.x() + d.getDx(), curr.point.y() + d.getDy());
                if (isValid(next, field) && !visited[next.x()][next.y()]) {
                    visited[next.x()][next.y()] = true;
                    queue.add(new Node(next, curr.firstDir));
                }
            }
        }

        return null;
    }

    private boolean isValid(Point p, FieldSnapshot field) {
        if (p.x() < 0 || p.x() >= field.getWidth()
                || p.y() < 0 || p.y() >= field.getHeight()) {
            return false;
        }
        if (field.getFoods().stream().anyMatch(f -> f.equals(p))) {
            return true;
        }
        return field.isPointFree(p);
    }

    private Direction fallback(Point head, Direction currentDir, FieldSnapshot field) {
        Direction[] dirs = {currentDir, getLeft(currentDir), getRight(currentDir)};
        for (Direction d : dirs) {
            Point next = new Point(head.x() + d.getDx(), head.y() + d.getDy());
            if (isValid(next, field)) {
                return d;
            }
        }
        return currentDir;
    }

    private Direction getLeft(Direction d) {
        return switch (d) {
            case UP -> Direction.LEFT;
            case LEFT -> Direction.DOWN;
            case DOWN -> Direction.RIGHT;
            case RIGHT -> Direction.UP;
        };
    }

    private Direction getRight(Direction d) {
        return switch (d) {
            case UP -> Direction.RIGHT;
            case RIGHT -> Direction.DOWN;
            case DOWN -> Direction.LEFT;
            case LEFT -> Direction.UP;
        };
    }

    private static class Node {
        Point point;
        Direction firstDir;

        Node(Point point, Direction firstDir) {
            this.point = point;
            this.firstDir = firstDir;
        }
    }
}
