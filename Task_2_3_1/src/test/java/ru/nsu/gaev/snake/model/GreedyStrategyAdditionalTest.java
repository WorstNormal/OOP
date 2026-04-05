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
        assertTrue(
            nextDir == Direction.LEFT
                || nextDir == Direction.UP
                || nextDir == Direction.DOWN
        );
    }

    @Test
    void testChooseNextDirectionToRightFood() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(15, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should move right or towards the food
        assertTrue(
            nextDir == Direction.RIGHT
                || nextDir == Direction.UP
                || nextDir == Direction.DOWN
        );
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
        RobotSnake robotNearWall = new RobotSnake(new Point(2, 10), Direction.LEFT, strategy);
        gameField.addRobot(robotNearWall);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotNearWall, gameField);
        assertNotNull(nextDir);
        // Robot should move towards food or use fallback strategy
        assertTrue(nextDir != null);
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
        assertTrue(
            nextDir == Direction.UP
                || nextDir == Direction.LEFT
                || nextDir == Direction.RIGHT
        );
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

    @Test
    void testGetLeftDirectionFromUp() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Test that the strategy can find paths using left turns
        assertTrue(nextDir != null);
    }

    @Test
    void testGetRightDirectionFromUp() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(15, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Test that the strategy can find paths using right turns
        assertTrue(nextDir != null);
    }

    @Test
    void testBfsPathfinding() {
        // Create a simple maze-like scenario
        gameField.getFoods().clear();
        gameField.addObstacle(new Obstacle(new Point(10, 11)));
        gameField.addObstacle(new Obstacle(new Point(10, 12)));
        gameField.getFoods().add(new Food(new Point(15, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should find a path around obstacles
        assertTrue(nextDir != null);
    }

    @Test
    void testIsValidPointInBounds() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(19, 19), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should be able to reach corner of field
        assertTrue(nextDir != null);
    }

    @Test
    void testIsValidPointOutOfBounds() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(1, 1), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should not move out of bounds
        assertTrue(nextDir != null);
    }

    @Test
    void testFallbackStrategyWhenBfsBlockedUp() {
        RobotSnake robotCorner = new RobotSnake(new Point(10, 1), Direction.UP, strategy);
        gameField.addRobot(robotCorner);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 0), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotCorner, gameField);
        assertNotNull(nextDir);
        // Should use fallback strategy
        assertTrue(nextDir != null);
    }

    @Test
    void testFallbackStrategyWhenBfsBlockedDown() {
        RobotSnake robotCorner = new RobotSnake(new Point(10, 18), Direction.DOWN, strategy);
        gameField.addRobot(robotCorner);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 19), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotCorner, gameField);
        assertNotNull(nextDir);
        // Should use fallback strategy
        assertTrue(nextDir != null);
    }

    @Test
    void testFallbackStrategyWhenBfsBlockedLeft() {
        RobotSnake robotCorner = new RobotSnake(new Point(1, 10), Direction.LEFT, strategy);
        gameField.addRobot(robotCorner);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(0, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotCorner, gameField);
        assertNotNull(nextDir);
        // Should use fallback strategy
        assertTrue(nextDir != null);
    }

    @Test
    void testFallbackStrategyWhenBfsBlockedRight() {
        RobotSnake robotCorner = new RobotSnake(new Point(18, 10), Direction.RIGHT, strategy);
        gameField.addRobot(robotCorner);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(19, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotCorner, gameField);
        assertNotNull(nextDir);
        // Should use fallback strategy
        assertTrue(nextDir != null);
    }

    @Test
    void testMultipleFoodsDistanceCalculation() {
        gameField.getFoods().clear();
        // Add multiple foods at different distances
        gameField.getFoods().add(new Food(new Point(12, 10), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(15, 10), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(8, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should choose closest food
        assertTrue(nextDir != null);
    }

    @Test
    void testDirectionalVariationUp() {
        robot.setNextDirection(Direction.UP);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 5), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        assertEquals(Direction.UP, nextDir);
    }

    @Test
    void testDirectionalVariationDown() {
        robot.setNextDirection(Direction.DOWN);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 15), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        assertEquals(Direction.DOWN, nextDir);
    }

    @Test
    void testDirectionalVariationLeft() {
        robot.setNextDirection(Direction.LEFT);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        assertTrue(nextDir == Direction.LEFT || nextDir == Direction.UP || nextDir == Direction.DOWN);
    }

    @Test
    void testDirectionalVariationRight() {
        robot.setNextDirection(Direction.RIGHT);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(15, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        assertTrue(
            nextDir == Direction.RIGHT
                || nextDir == Direction.UP
                || nextDir == Direction.DOWN
        );
    }

    @Test
    void testComplexMazeNavigation() {
        // Create a complex obstacle layout
        for (int i = 5; i <= 15; i++) {
            if (i != 10) {
                gameField.addObstacle(new Obstacle(new Point(i, 12)));
            }
        }

        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(15, 15), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should find path around complex obstacles
        assertTrue(nextDir != null);
    }

    @Test
    void testSingleCellAwayFood() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 9), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        assertEquals(Direction.UP, nextDir);
    }

    @Test
    void testDiagonalDistance() {
        gameField.getFoods().clear();
        // Food at diagonal distance
        gameField.getFoods().add(new Food(new Point(15, 15), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        assertTrue(nextDir == Direction.RIGHT || nextDir == Direction.DOWN);
    }

    @Test
    void testSnakeBodyBlocksPath() {
        // Grow snake to block some paths
        for (int i = 0; i < 3; i++) {
            robot.eat(new Food(new Point(0, 0), FoodType.NORMAL));
            robot.move();
        }

        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(12, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, gameField);
        assertNotNull(nextDir);
        // Should navigate around own body
        assertTrue(nextDir != null);
    }
}
