package ru.nsu.gaev.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.FoodType;
import ru.nsu.gaev.snake.model.common.Level;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.core.GameField;
import ru.nsu.gaev.snake.model.entity.Food;
import ru.nsu.gaev.snake.model.entity.Obstacle;
import ru.nsu.gaev.snake.model.entity.RobotSnake;
import ru.nsu.gaev.snake.model.strategy.GreedyStrategy;

/**
 * Дополнительные тесты для GameField для повышения покрытия.
 */
class GameFieldCoverageTest {
    private GameField field;
    private Level level;

    @BeforeEach
    void setUp() {
        level = new Level(1, 1000, 200);
        field = new GameField(30, 30, 3, level);
    }

    @Test
    void testUpdateWhenGameNotStarted() {
        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(15, 15), FoodType.NORMAL));

        Point initialHead = field.getPlayer().getHead();
        field.update();

        assertEquals(initialHead, field.getPlayer().getHead());
    }

    @Test
    void testUpdateWhenGameOver() {
        field.setStarted(true);
        field.getPlayer().kill();

        Point headBeforeUpdate = field.getPlayer().getHead();
        field.update();

        assertEquals(headBeforeUpdate, field.getPlayer().getHead());
    }

    @Test
    void testRobotDeadDontMove() {
        RobotSnake robot = new RobotSnake(
            new Point(20, 20),
            Direction.UP,
            new GreedyStrategy()
        );
        field.addRobot(robot);
        robot.kill();
        field.setStarted(true);

        Point deadPosition = robot.getHead();
        field.update();

        assertEquals(deadPosition, robot.getHead());
    }

    @Test
    void testModelListenerNotification() {
        boolean[] tickCalled = {false};

        field.addModelListener(new ru.nsu.gaev.snake.model.core.ModelListener() {
            @Override
            public void onTick(ru.nsu.gaev.snake.model.core.FieldSnapshot snapshot) {
                tickCalled[0] = true;
            }

            @Override
            public void onCollision(ru.nsu.gaev.snake.model.core.FieldSnapshot snapshot) {
            }
        });

        field.setStarted(true);
        field.update();

        assertTrue(tickCalled[0]);
    }

    @Test
    void testGameFieldListenerNotification() {
        boolean[] called = {false};

        field.addListener(gameFieldView -> {
            called[0] = true;
        });

        field.setStarted(true);

        assertTrue(called[0]);
    }

    @Test
    void testRemoveListener() {
        boolean[] called = {false};

        ru.nsu.gaev.snake.model.core.GameFieldListener listener = gameFieldView -> {
            called[0] = true;
        };

        field.addListener(listener);
        field.removeListener(listener);
        field.setStarted(true);

        assertFalse(called[0]);
    }

    @Test
    void testRemoveModelListener() {
        boolean[] called = {false};

        ru.nsu.gaev.snake.model.core.ModelListener listener = new ru.nsu.gaev.snake.model.core.ModelListener() {
            @Override
            public void onTick(ru.nsu.gaev.snake.model.core.FieldSnapshot snapshot) {
                called[0] = true;
            }

            @Override
            public void onCollision(ru.nsu.gaev.snake.model.core.FieldSnapshot snapshot) {
            }
        };

        field.addModelListener(listener);
        field.removeModelListener(listener);
        field.setStarted(true);
        field.update();

        assertFalse(called[0]);
    }

    @Test
    void testCurrentLevelGetterAndSetter() {
        Level level2 = new Level(2, 500, 300);
        field.setCurrentLevel(level2);

        assertEquals(level2, field.getCurrentLevel());
    }

    @Test
    void testIsPointFreeWithFood() {
        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(10, 10), FoodType.NORMAL));

        assertFalse(field.isPointFree(new Point(10, 10)));
    }

    @Test
    void testIsPointFreeWithObstacle() {
        field.addObstacle(new Obstacle(new Point(10, 10)));

        assertFalse(field.isPointFree(new Point(10, 10)));
    }

    @Test
    void testGameFieldState() {
        assertNotNull(field.getPlayer());
        assertTrue(field.getFoods().size() > 0);
        assertFalse(field.isGameOver());
        assertFalse(field.isGameWon());
        assertFalse(field.isGameDraw());
        assertFalse(field.isStarted());
    }

    @Test
    void testAddMultipleRobots() {
        RobotSnake robot1 = new RobotSnake(new Point(10, 10), Direction.UP, new GreedyStrategy());
        RobotSnake robot2 = new RobotSnake(new Point(20, 20), Direction.DOWN, new GreedyStrategy());

        field.addRobot(robot1);
        field.addRobot(robot2);

        assertEquals(2, field.getRobots().size());
    }

    @Test
    void testAddMultipleObstacles() {
        field.addObstacle(new Obstacle(new Point(5, 5)));
        field.addObstacle(new Obstacle(new Point(10, 10)));
        field.addObstacle(new Obstacle(new Point(15, 15)));

        assertEquals(3, field.getObstacles().size());
    }

    @Test
    void testIsPointFreeWithPlayer() {
        Point playerHead = field.getPlayer().getHead();
        assertFalse(field.isPointFree(playerHead));
    }

    @Test
    void testGameScoreInitial() {
        assertEquals(0, field.getScore());
    }

    @Test
    void testSetStarted() {
        assertFalse(field.isStarted());
        field.setStarted(true);
        assertTrue(field.isStarted());
    }

    @Test
    void testPlayerInitialPosition() {
        Point playerHead = field.getPlayer().getHead();
        assertNotNull(playerHead);
        assertTrue(playerHead.x() >= 0 && playerHead.x() < 30);
        assertTrue(playerHead.y() >= 0 && playerHead.y() < 30);
    }
}
