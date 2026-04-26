package ru.nsu.gaev.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ModelTest {
    @Test
    void testPoint() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        assertEquals(1, p1.x());
        assertEquals(2, p1.y());
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());
    }

    @Test
    void testDirection() {
        assertEquals(0, Direction.UP.getDx());
        assertEquals(-1, Direction.UP.getDy());
        assertTrue(Direction.UP.isOpposite(Direction.DOWN));
        assertTrue(Direction.LEFT.isOpposite(Direction.RIGHT));
        assertFalse(Direction.UP.isOpposite(Direction.LEFT));

        assertEquals(1, Direction.RIGHT.getDx());
        assertEquals(0, Direction.RIGHT.getDy());
    }

    @Test
    void testFoodAndLevelAndObstacle() {
        Food food = new Food(new Point(0, 0), FoodType.NORMAL);
        assertEquals(new Point(0, 0), food.position());
        assertEquals(FoodType.NORMAL, food.type());

        Level level = new Level(2, 500, 150);
        assertEquals(2, level.levelNumber());
        assertEquals(500, level.targetScore());

        Obstacle obs = new Obstacle(new Point(3, 3));
        assertEquals(new Point(3, 3), obs.position());
    }

    @Test
    void testRobotSnake() {
        RobotStrategy dummy = (r, f) -> Direction.DOWN;
        RobotSnake rs = new RobotSnake(new Point(0, 0), Direction.RIGHT, dummy);

        GameField field = new GameField(10, 10, 0, new Level(1, 100, 200));
        rs.determineNextMove(field);
        rs.move();
        assertEquals(Direction.DOWN, rs.getCurrentDirection());
    }

    @Test
    void testRandomStrategy() {
        RandomStrategy rs = new RandomStrategy();
        RobotSnake robot = new RobotSnake(new Point(1, 1), Direction.RIGHT, rs);
        GameField field = new GameField(10, 10, 0, new Level(1, 100, 200));
        Direction d = rs.chooseNextDirection(robot, field);
        assertNotNull(d);

        field.addObstacle(new Obstacle(new Point(2, 1)));
        field.addObstacle(new Obstacle(new Point(1, 0)));
        field.addObstacle(new Obstacle(new Point(1, 2)));
        Direction d2 = rs.chooseNextDirection(robot, field);
        assertNotNull(d2);
    }
}
