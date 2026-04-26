package ru.nsu.gaev.snake.model.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.core.GameFieldView;
import ru.nsu.gaev.snake.model.entity.RobotSnake;

/**
 * Случайная стратегия выбора безопасного хода.
 */
public class RandomStrategy implements RobotStrategy {
    private static final Random RANDOM = new Random();

    @Override
    public Direction chooseNextDirection(RobotSnake robot, GameFieldView field) {
        Direction forward = robot.getCurrentDirection();
        Direction left = getLeft(forward);
        Direction right = getRight(forward);

        List<Direction> possible = new ArrayList<>();
        if (isSafe(robot.getHead(), forward, field)) {
            possible.add(forward);
        }
        if (isSafe(robot.getHead(), left, field)) {
            possible.add(left);
        }
        if (isSafe(robot.getHead(), right, field)) {
            possible.add(right);
        }

        if (possible.isEmpty()) {
            return forward;
        }
        return possible.get(RANDOM.nextInt(possible.size()));
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

    private boolean isSafe(Point head, Direction dir, GameFieldView field) {
        Point p = new Point(head.x() + dir.getDx(), head.y() + dir.getDy());
        if (p.x() < 0 || p.x() >= field.getWidth() || p.y() < 0 || p.y() >= field.getHeight()) {
            return false;
        }

        return field.isPointFree(p)
                || field.getFoods().stream().anyMatch(f -> f.position().equals(p));
    }
}
