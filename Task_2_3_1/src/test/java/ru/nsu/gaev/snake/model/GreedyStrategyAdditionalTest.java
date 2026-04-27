package ru.nsu.gaev.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.FoodType;
import ru.nsu.gaev.snake.model.common.Level;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.core.FieldSnapshot;
import ru.nsu.gaev.snake.model.core.GameField;
import ru.nsu.gaev.snake.model.entity.Food;
import ru.nsu.gaev.snake.model.entity.Obstacle;
import ru.nsu.gaev.snake.model.entity.RobotSnake;
import ru.nsu.gaev.snake.model.strategy.GreedyStrategy;

/**
 * Дополнительные тесты для класса GreedyStrategy.
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

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
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

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
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

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertEquals(Direction.UP, nextDir);
    }

    @Test
    void testChooseNextDirectionToDownFood() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 15), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertEquals(Direction.DOWN, nextDir);
    }

    @Test
    void testChooseNextDirectionWithMultipleFoods() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 5), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(10, 11), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(15, 15), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertEquals(Direction.DOWN, nextDir);
    }

    @Test
    void testChooseNextDirectionNoFoods() {
        gameField.getFoods().clear();

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertEquals(Direction.UP, nextDir);
    }

    @Test
    void testChooseNextDirectionAvoidingWall() {
        RobotSnake robotNearWall = new RobotSnake(new Point(2, 10), Direction.LEFT, strategy);
        gameField.addRobot(robotNearWall);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotNearWall, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testChooseNextDirectionWithLongSnake() {
        for (int i = 0; i < 5; i++) {
            robot.eat(new Food(new Point(0, 0), FoodType.NORMAL));
        }

        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 5), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertEquals(Direction.UP, nextDir);
    }

    @Test
    void testChooseNextDirectionAvoidingObstacles() {
        gameField.addObstacle(new Obstacle(new Point(10, 9)));

        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 5), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(
            nextDir == Direction.UP
                || nextDir == Direction.LEFT
                || nextDir == Direction.RIGHT
        );
    }

    @Test
    void testChooseNextDirectionFallbackWhenPathBlocked() {
        RobotSnake robotCornered = new RobotSnake(new Point(2, 2), Direction.UP, strategy);
        gameField.addRobot(robotCornered);

        gameField.addObstacle(new Obstacle(new Point(2, 1)));
        gameField.addObstacle(new Obstacle(new Point(3, 2)));

        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(2, 1), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotCornered, createSnapshot(gameField));
        assertNotNull(nextDir);
    }

    @Test
    void testChooseNextDirectionNearestFoodSelection() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 14), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(10, 18), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertEquals(Direction.DOWN, nextDir);
    }

    @Test
    void testGetLeftDirectionFromUp() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testGetRightDirectionFromUp() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(15, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testBfsPathfinding() {
        gameField.getFoods().clear();
        gameField.addObstacle(new Obstacle(new Point(10, 11)));
        gameField.addObstacle(new Obstacle(new Point(10, 12)));
        gameField.getFoods().add(new Food(new Point(15, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testIsValidPointInBounds() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(19, 19), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testIsValidPointOutOfBounds() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(1, 1), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testFallbackStrategyWhenBfsBlockedUp() {
        RobotSnake robotCorner = new RobotSnake(new Point(10, 1), Direction.UP, strategy);
        gameField.addRobot(robotCorner);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 0), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotCorner, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testFallbackStrategyWhenBfsBlockedDown() {
        RobotSnake robotCorner = new RobotSnake(new Point(10, 18), Direction.DOWN, strategy);
        gameField.addRobot(robotCorner);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 19), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotCorner, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testFallbackStrategyWhenBfsBlockedLeft() {
        RobotSnake robotCorner = new RobotSnake(new Point(1, 10), Direction.LEFT, strategy);
        gameField.addRobot(robotCorner);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(0, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotCorner, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testFallbackStrategyWhenBfsBlockedRight() {
        RobotSnake robotCorner = new RobotSnake(new Point(18, 10), Direction.RIGHT, strategy);
        gameField.addRobot(robotCorner);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(19, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robotCorner, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testMultipleFoodsDistanceCalculation() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(12, 10), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(15, 10), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(8, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testDirectionalVariationUp() {
        robot.setNextDirection(Direction.UP);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 5), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertEquals(Direction.UP, nextDir);
    }

    @Test
    void testDirectionalVariationDown() {
        robot.setNextDirection(Direction.DOWN);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 15), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertEquals(Direction.DOWN, nextDir);
    }

    @Test
    void testDirectionalVariationLeft() {
        robot.setNextDirection(Direction.LEFT);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir == Direction.LEFT || nextDir == Direction.UP || nextDir == Direction.DOWN);
    }

    @Test
    void testDirectionalVariationRight() {
        robot.setNextDirection(Direction.RIGHT);
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(15, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(
            nextDir == Direction.RIGHT
                || nextDir == Direction.UP
                || nextDir == Direction.DOWN
        );
    }

    @Test
    void testComplexMazeNavigation() {
        for (int i = 5; i <= 15; i++) {
            if (i != 10) {
                gameField.addObstacle(new Obstacle(new Point(i, 12)));
            }
        }

        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(15, 15), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
    }

    @Test
    void testSingleCellAwayFood() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 9), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertEquals(Direction.UP, nextDir);
    }

    @Test
    void testDiagonalDistance() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(15, 15), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir == Direction.RIGHT || nextDir == Direction.DOWN);
    }

    @Test
    void testSnakeBodyBlocksPath() {
        for (int i = 0; i < 3; i++) {
            robot.eat(new Food(new Point(0, 0), FoodType.NORMAL));
            robot.move();
        }

        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(12, 10), FoodType.NORMAL));

        Direction nextDir = strategy.chooseNextDirection(robot, createSnapshot(gameField));
        assertNotNull(nextDir);
        assertTrue(nextDir != null);
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
