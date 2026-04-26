package ru.nsu.gaev.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.FoodType;
import ru.nsu.gaev.snake.model.common.Level;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.core.FieldSnapshot;
import ru.nsu.gaev.snake.model.core.GameField;
import ru.nsu.gaev.snake.model.entity.Food;
import ru.nsu.gaev.snake.model.entity.RobotSnake;
import ru.nsu.gaev.snake.model.strategy.GreedyStrategy;

class GreedyStrategyTest {
    @Test
    void testChooseNextDirection() {
        Level startLevel = new Level(1, 100, 200);
        GameField field = new GameField(10, 10, 0, startLevel);
        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(0, 5), FoodType.NORMAL));

        RobotSnake robot = new RobotSnake(new Point(5, 5), Direction.LEFT, new GreedyStrategy());
        field.addRobot(robot);

        GreedyStrategy strategy = new GreedyStrategy();
        // Создаем snapshot для тестирования
        FieldSnapshot snapshot = createSnapshot(field);
        Direction nextDir = strategy.chooseNextDirection(robot, snapshot);
        assertEquals(Direction.LEFT, nextDir);

        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(5, 0), FoodType.NORMAL));
        snapshot = createSnapshot(field);
        nextDir = strategy.chooseNextDirection(robot, snapshot);
        assertEquals(Direction.UP, nextDir);

        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(5, 9), FoodType.NORMAL));
        snapshot = createSnapshot(field);
        nextDir = strategy.chooseNextDirection(robot, snapshot);
        assertNotNull(nextDir);
    }

    private FieldSnapshot createSnapshot(GameField field) {
        return new FieldSnapshot() {
            @Override
            public int getWidth() {
                return field.getWidth();
            }

            @Override
            public int getHeight() {
                return field.getHeight();
            }

            @Override
            public java.util.List<Point> getObstacles() {
                java.util.List<Point> points = new java.util.ArrayList<>();
                for (var obs : field.getObstacles()) {
                    points.add(obs.position());
                }
                return java.util.Collections.unmodifiableList(points);
            }

            @Override
            public java.util.List<Point> getFoods() {
                java.util.List<Point> points = new java.util.ArrayList<>();
                for (var food : field.getFoods()) {
                    points.add(food.position());
                }
                return java.util.Collections.unmodifiableList(points);
            }

            @Override
            public boolean isPointFree(Point point) {
                return field.isPointFree(point);
            }

            @Override
            public Point getMyPosition() {
                return field.getPlayer().getHead();
            }
        };
    }
}
