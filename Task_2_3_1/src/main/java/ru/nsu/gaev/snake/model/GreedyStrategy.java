package ru.nsu.gaev.snake.model;

import java.util.LinkedList;
import java.util.Queue;

public class GreedyStrategy implements RobotStrategy {
    
    @Override
    public Direction chooseNextDirection(RobotSnake robot, GameField field) {
        Point head = robot.getHead();
        
        Food nearestFood = null;
        int minDistance = Integer.MAX_VALUE;
        for (Food f : field.getFoods()) {
            int dist = Math.abs(f.getPosition().x() - head.x()) + Math.abs(f.getPosition().y() - head.y());
            if (dist < minDistance) {
                minDistance = dist;
                nearestFood = f;
            }
        }
        
        if (nearestFood == null) {
            return fallback(head, robot.getCurrentDirection(), field);
        }
        
        Direction bestDir = bfs(head, nearestFood.getPosition(), field, robot);
        if (bestDir != null) {
            return bestDir;
        }
        
        return fallback(head, robot.getCurrentDirection(), field);
    }
    
    private Direction bfs(Point start, Point target, GameField field, RobotSnake robot) {
        boolean[][] visited = new boolean[field.getWidth()][field.getHeight()];
        Queue<Node> queue = new LinkedList<>();
        
        visited[start.x()][start.y()] = true;
        
        for (Direction d : Direction.values()) {
            if (robot.getBody().size() > 1 && d.isOpposite(robot.getCurrentDirection())) continue;
            Point next = new Point(start.x() + d.getDx(), start.y() + d.getDy());
            if (isValid(next, field)) {
                queue.add(new Node(next, d));
                visited[next.x()][next.y()] = true;
            }
        }
        
        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            if (curr.p.equals(target)) {
                return curr.firstDir;
            }
            
            for (Direction d : Direction.values()) {
                Point next = new Point(curr.p.x() + d.getDx(), curr.p.y() + d.getDy());
                if (isValid(next, field) && !visited[next.x()][next.y()]) {
                    visited[next.x()][next.y()] = true;
                    queue.add(new Node(next, curr.firstDir));
                }
            }
        }
        
        return null;
    }
    
    private boolean isValid(Point p, GameField field) {
        if (p.x() < 0 || p.x() >= field.getWidth() || p.y() < 0 || p.y() >= field.getHeight()) return false;
        if (field.getFoods().stream().anyMatch(f -> f.getPosition().equals(p))) return true;
        return field.isPointFree(p);
    }
    
    private Direction fallback(Point head, Direction currentDir, GameField field) {
        Direction[] dirs = {currentDir, getLeft(currentDir), getRight(currentDir)};
        for (Direction d : dirs) {
            Point next = new Point(head.x() + d.getDx(), head.y() + d.getDy());
            if (isValid(next, field)) {
                return d;
            }
        }
        return currentDir; // unavoidable crash
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
        Point p;
        Direction firstDir;
        Node(Point p, Direction firstDir) {
            this.p = p;
            this.firstDir = firstDir;
        }
    }
}
