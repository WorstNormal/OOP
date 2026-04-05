package ru.nsu.gaev.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class GreedyStrategyTest {
    @Test
    void testChooseNextDirection() {
        Level startLevel = new Level(1, 100, 200);
        GameField field = new GameField(10, 10, 0, startLevel); // no foods by default if 0
        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(0, 5), FoodType.NORMAL));

        RobotSnake robot = new RobotSnake(new Point(5, 5), Direction.LEFT, new GreedyStrategy());
        field.addRobot(robot);

        GreedyStrategy strategy = new GreedyStrategy();
        Direction nextDir = strategy.chooseNextDirection(robot, field);
        assertEquals(Direction.LEFT, nextDir);

        // Food above
        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(5, 0), FoodType.NORMAL));
        nextDir = strategy.chooseNextDirection(robot, field);
        assertEquals(Direction.UP, nextDir);

        // Fallback test
        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(5, 9), FoodType.NORMAL));
        nextDir = strategy.chooseNextDirection(robot, field);
        // Will go DOWN or LEFT based on BFS priorities / shortest path
        assertNotNull(nextDir);
    }
}
