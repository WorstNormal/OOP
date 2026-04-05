package ru.nsu.gaev.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Additional tests for GreedyStrategy class.
 */
class GreedyStrategyAdditionalTest {
    private GameField gameField;
    private RobotSnake robot;
    private GreedyStrategy strategy;
    private Level level;

    @BeforeEach
    void setUp() {
        level = new Level(1, 100, 200);
        gameField = new GameField(20, 20, 0, level);
        strategy = new GreedyStrategy();
        robot = new RobotSnake(new Point(10, 10), Direction.UP, strategy);
        gameField.addRobot(robot);
    }

    @Test
    void testChooseNextDirectionToLeftFood() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should move left or towards the food
        assertTrue(nextDir == Direction.LEFT || nextDir == Direction.UP || nextDir == Direction.DOWN);
    }

    @Test
    void testChooseNextDirectionToRightFood() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(15, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should move right or towards the food
        assertTrue(nextDir == Direction.RIGHT || nextDir == Direction.UP || nextDir == Direction.DOWN);
    }

    @Test
    void testChooseNextDirectionToUpFood() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 5), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        assertEquals(Direction.UP, nextDir);
    }

    @Test
    void testChooseNextDirectionToDownFood() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 15), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        assertEquals(Direction.DOWN, nextDir);
    }

    @Test
    void testChooseNextDirectionWithMultipleFoods() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 5), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(10, 11), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(15, 15), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should choose the nearest food at (10, 11)
        assertEquals(Direction.DOWN, nextDir);
    }

    @Test
    void testChooseNextDirectionNoFoods() {
        gameField.getFoods().clear();

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should use fallback direction (same direction)
        assertEquals(Direction.UP, nextDir);
    }

    @Test
    void testChooseNextDirectionAvoidingWall() {
        RobotSnake robotNearWall = new RobotSnake(new Point(1, 10), Direction.LEFT, strategy);
        gameField.addRobot(robotNearWall);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(0, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotNearWall, gameField);
        assertNotNull(nextDir);
        // Should avoid moving to the wall
        assertTrue(nextDir != Direction.LEFT);
    }

    @Test
    void testChooseNextDirectionWithLongSnake() {
        // Make the robot's snake longer
        for (int i = 0; i < 5; i++) {
            robot.eat(new Food(new Point(0, 0), FoodType.NORMAL));
        }

        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 5), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should still find a path to food
        assertEquals(Direction.UP, nextDir);
    }

    @Test
    void testChooseNextDirectionAvoidingObstacles() {
        gameField.addObstacle(new Obstacle(new Point(10, 9)));

        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 5), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should find alternative path avoiding obstacle
        assertTrue(nextDir == Direction.UP || nextDir == Direction.LEFT || nextDir == Direction.RIGHT);
    }

    @Test
    void testChooseNextDirectionFallbackWhenPathBlocked() {
        // Create a scenario where robot is surrounded
        RobotSnake robotCornered = new RobotSnake(new Point(2, 2), Direction.UP, strategy);
        gameField.addRobot(robotCornered);

        // Add obstacles around
        gameField.addObstacle(new Obstacle(new Point(2, 1)));
        gameField.addObstacle(new Obstacle(new Point(3, 2)));

        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(2, 1), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotCornered, gameField);
        assertNotNull(nextDir);
    }

    @Test
    void testChooseNextDirectionNearestFoodSelection() {
        gameField.getFoods().clear();
        // Food 1: distance = 4
        gameField.getFoods().add(new Food(new Point(10, 14), FoodType.NORMAL));
        // Food 2: distance = 8
        gameField.getFoods().add(new Food(new Point(10, 18), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should target the nearer food
        assertEquals(Direction.DOWN, nextDir);
    }
}

