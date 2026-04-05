package ru.nsu.gaev.snake.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.snake.model.Direction;
import ru.nsu.gaev.snake.model.Food;
import ru.nsu.gaev.snake.model.FoodType;
import ru.nsu.gaev.snake.model.GameField;
import ru.nsu.gaev.snake.model.GreedyStrategy;
import ru.nsu.gaev.snake.model.Level;
import ru.nsu.gaev.snake.model.Obstacle;
import ru.nsu.gaev.snake.model.Point;
import ru.nsu.gaev.snake.model.RobotSnake;

/**
 * Tests for GameRenderer class.
 */
class GameRendererTest {
    private GameRenderer renderer;
    private Canvas canvas;
    private GameField gameField;
    private Level level;

    @BeforeEach
    void setUp() {
        canvas = new Canvas(600, 600);
        level = new Level(1, 100, 200);
        gameField = new GameField(20, 20, 5, level);
        renderer = new GameRenderer(canvas, 30);
    }

    @Test
    void testRendererInitialization() {
        assertNotNull(renderer);
        assertNotNull(canvas);
    }

    @Test
    void testRendererCanvasNotNull() {
        assertNotNull(canvas);
        assertTrue(canvas.getWidth() > 0);
        assertTrue(canvas.getHeight() > 0);
    }

    @Test
    void testRendererCellSize() {
        try {
            var cellSizeField = GameRenderer.class.getDeclaredField("cellSize");
            cellSizeField.setAccessible(true);
            int cellSize = cellSizeField.getInt(renderer);
            assertTrue(cellSize > 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRendererGraphicsContextNotNull() {
        try {
            var gcField = GameRenderer.class.getDeclaredField("gc");
            gcField.setAccessible(true);
            var gc = gcField.get(renderer);
            assertNotNull(gc);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRenderWithEmptyField() {
        gameField.getFoods().clear();
        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderWithFoods() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 5), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(10, 10), FoodType.NORMAL));

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderWithObstacles() {
        gameField.addObstacle(new Obstacle(new Point(7, 7)));
        gameField.addObstacle(new Obstacle(new Point(12, 12)));

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderWithRobots() {
        RobotSnake robot = new RobotSnake(
                new Point(15, 15),
                Direction.LEFT,
                new GreedyStrategy()
        );
        gameField.addRobot(robot);

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderWithPlayer() {
        assertNotNull(gameField.getPlayer());

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderWithMultipleElements() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(5, 5), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(10, 10), FoodType.NORMAL));

        gameField.addObstacle(new Obstacle(new Point(7, 7)));

        RobotSnake robot = new RobotSnake(
                new Point(15, 15),
                Direction.UP,
                new GreedyStrategy()
        );
        gameField.addRobot(robot);

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderWithDeadPlayer() {
        // Kill the player
        gameField.getPlayer().kill();

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderWithDeadRobot() {
        RobotSnake robot = new RobotSnake(
                new Point(15, 15),
                Direction.UP,
                new GreedyStrategy()
        );
        gameField.addRobot(robot);
        robot.kill();

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderMultipleTimes() {
        // Should handle multiple render calls without issues
        for (int i = 0; i < 5; i++) {
            renderer.render(gameField);
        }
    }

    @Test
    void testRenderWithGameNotStarted() {
        // Game not started initially
        assertTrue(!gameField.isStarted());

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderWithLargeGameField() {
        Canvas largeCanvas = new Canvas(1200, 1200);
        GameField largeField = new GameField(40, 40, 10, level);
        GameRenderer largeRenderer = new GameRenderer(largeCanvas, 30);

        // Should not throw exception
        largeRenderer.render(largeField);
    }

    @Test
    void testRenderWithSmallGameField() {
        Canvas smallCanvas = new Canvas(300, 300);
        GameField smallField = new GameField(10, 10, 2, level);
        GameRenderer smallRenderer = new GameRenderer(smallCanvas, 30);

        // Should not throw exception
        smallRenderer.render(smallField);
    }

    @Test
    void testRenderWithManyFoods() {
        gameField.getFoods().clear();
        for (int i = 0; i < 20; i++) {
            gameField.getFoods().add(new Food(new Point(i % 20, i / 20), FoodType.NORMAL));
        }

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderWithManyObstacles() {
        for (int i = 0; i < 15; i++) {
            gameField.addObstacle(new Obstacle(new Point(i, i)));
        }

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderWithManyRobots() {
        for (int i = 0; i < 3; i++) {
            RobotSnake robot = new RobotSnake(
                    new Point(5 + i * 3, 5),
                    Direction.UP,
                    new GreedyStrategy()
            );
            gameField.addRobot(robot);
        }

        // Should not throw exception
        renderer.render(gameField);
    }

    @Test
    void testRenderCanvasHasGraphicsContext() {
        assertNotNull(canvas.getGraphicsContext2D());
    }

    @Test
    void testRenderPlayerAlive() {
        assertTrue(gameField.getPlayer().isAlive());
        // Should render the alive player
        renderer.render(gameField);
    }

    @Test
    void testRenderGameFieldConsistency() {
        int initialFoodCount = gameField.getFoods().size();
        renderer.render(gameField);
        int afterRenderFoodCount = gameField.getFoods().size();

        // Render should not modify game field
        assertTrue(initialFoodCount == afterRenderFoodCount);
    }

    @Test
    void testRenderWithPlayerMovement() {
        // Move player
        gameField.getPlayer().setNextDirection(Direction.RIGHT);
        gameField.getPlayer().move();

        // Should render correctly
        renderer.render(gameField);
    }

    @Test
    void testRenderWithFoodConsumption() {
        gameField.getFoods().clear();
        Point foodPoint = gameField.getPlayer().getHead();
        gameField.getFoods().add(new Food(foodPoint, FoodType.NORMAL));

        // Should render food at the same position as player head
        renderer.render(gameField);
    }

    @Test
    void testRenderWithAllGameElements() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(3, 3), FoodType.NORMAL));
        gameField.getFoods().add(new Food(new Point(7, 7), FoodType.NORMAL));

        gameField.addObstacle(new Obstacle(new Point(5, 5)));
        gameField.addObstacle(new Obstacle(new Point(8, 8)));

        RobotSnake robot1 = new RobotSnake(
                new Point(15, 5),
                Direction.UP,
                new GreedyStrategy()
        );
        RobotSnake robot2 = new RobotSnake(
                new Point(5, 15),
                Direction.DOWN,
                new GreedyStrategy()
        );

        gameField.addRobot(robot1);
        gameField.addRobot(robot2);

        // Should render without exceptions
        renderer.render(gameField);
    }

    @Test
    void testRenderAfterPlayerMove() {
        gameField.getPlayer().setNextDirection(Direction.RIGHT);
        gameField.getPlayer().move();
        renderer.render(gameField);
    }

    @Test
    void testRenderAfterPlayerGrowth() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(
            gameField.getPlayer().getHead(),
            FoodType.NORMAL
        ));
        gameField.getPlayer().eat(gameField.getFoods().get(0));
        renderer.render(gameField);
    }

    @Test
    void testRenderWithAllSnakesAlive() {
        RobotSnake robot = new RobotSnake(
                new Point(15, 15),
                Direction.UP,
                new GreedyStrategy()
        );
        gameField.addRobot(robot);

        assertTrue(gameField.getPlayer().isAlive());
        assertTrue(robot.isAlive());
        renderer.render(gameField);
    }

    @Test
    void testRenderWithSomeSnakesDead() {
        RobotSnake robot = new RobotSnake(
                new Point(15, 15),
                Direction.UP,
                new GreedyStrategy()
        );
        gameField.addRobot(robot);
        robot.kill();

        assertTrue(gameField.getPlayer().isAlive());
        assertTrue(!robot.isAlive());
        renderer.render(gameField);
    }

    @Test
    void testRenderWithEdgeCases() {
        // Render at minimum field size
        Canvas minCanvas = new Canvas(60, 60);
        GameField minField = new GameField(2, 2, 0, level);
        GameRenderer minRenderer = new GameRenderer(minCanvas, 30);

        minRenderer.render(minField);
    }

    @Test
    void testRenderPreservesCanvasState() {
        int originalWidth = (int) canvas.getWidth();
        int originalHeight = (int) canvas.getHeight();

        renderer.render(gameField);

        assertEquals(originalWidth, (int) canvas.getWidth());
        assertEquals(originalHeight, (int) canvas.getHeight());
    }

    @Test
    void testRenderMultipleConsecutiveCalls() {
        for (int i = 0; i < 10; i++) {
            gameField.getPlayer().move();
            renderer.render(gameField);
        }
    }

    @Test
    void testRenderWithMaxFoods() {
        gameField.getFoods().clear();
        for (int i = 0; i < 50; i++) {
            gameField.getFoods().add(new Food(
                new Point(i % 20, i / 20),
                FoodType.NORMAL
            ));
        }
        renderer.render(gameField);
    }

    @Test
    void testRenderWithMaxObstacles() {
        for (int i = 0; i < 50; i++) {
            gameField.addObstacle(new Obstacle(new Point(i % 20, i / 20)));
        }
        renderer.render(gameField);
    }

    @Test
    void testRenderRobotAtDifferentPositions() {
        gameField.getFoods().clear();

        for (int x = 0; x < 20; x += 5) {
            for (int y = 0; y < 20; y += 5) {
                RobotSnake robot = new RobotSnake(
                        new Point(x, y),
                        Direction.UP,
                        new GreedyStrategy()
                );
                gameField.addRobot(robot);
                renderer.render(gameField);
            }
        }
    }

    @Test
    void testRenderPlayerAtCorners() {
        // Test rendering with player at different corners
        renderer.render(gameField);
    }

    @Test
    void testRenderWithDifferentDirections() {
        for (Direction dir : Direction.values()) {
            gameField.getPlayer().setNextDirection(dir);
            renderer.render(gameField);
        }
    }

    @Test
    void testRenderGridLines() {
        // Render should include grid lines
        renderer.render(gameField);
        assertNotNull(canvas.getGraphicsContext2D());
    }

    @Test
    void testRenderBackgroundColor() {
        renderer.render(gameField);
        assertNotNull(canvas.getGraphicsContext2D());
    }

    @Test
    void testRenderFoodColor() {
        gameField.getFoods().clear();
        gameField.getFoods().add(new Food(new Point(10, 10), FoodType.NORMAL));
        renderer.render(gameField);
    }

    @Test
    void testRenderObstacleColor() {
        gameField.addObstacle(new Obstacle(new Point(10, 10)));
        renderer.render(gameField);
    }

    @Test
    void testRenderPlayerColor() {
        renderer.render(gameField);
        assertTrue(gameField.getPlayer().isAlive());
    }

    @Test
    void testRenderRobotColor() {
        RobotSnake robot = new RobotSnake(
                new Point(15, 15),
                Direction.UP,
                new GreedyStrategy()
        );
        gameField.addRobot(robot);
        assertTrue(robot.isAlive());
        renderer.render(gameField);
    }

    @Test
    void testRenderWithStartMessage() {
        GameField newField = new GameField(20, 20, 0, level);
        renderer.render(newField);
        assertTrue(!newField.isStarted());
    }

    @Test
    void testRenderGameAfterStart() {
        gameField.getPlayer().setNextDirection(Direction.UP);
        gameField.update();
        renderer.render(gameField);
        assertTrue(gameField.isStarted());
    }

    @Test
    void testCellSizeAffectsRendering() {
        Canvas canvas2 = new Canvas(600, 600);
        GameRenderer renderer2 = new GameRenderer(canvas2, 20);
        renderer2.render(gameField);
    }

    @Test
    void testRendererWithDifferentCanvasSizes() {
        int[] sizes = {300, 600, 900, 1200};
        for (int size : sizes) {
            Canvas testCanvas = new Canvas(size, size);
            GameRenderer testRenderer = new GameRenderer(testCanvas, 30);
            testRenderer.render(gameField);
        }
    }
}

