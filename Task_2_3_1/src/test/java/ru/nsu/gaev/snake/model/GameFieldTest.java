package ru.nsu.gaev.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameFieldTest {
    @Test
    void testFieldInitialization() {
        Level startLevel = new Level(1, 100, 200);
        GameField field = new GameField(20, 15, 2, startLevel);

        assertNotNull(field.getPlayer());
        assertEquals(2, field.getFoods().size());
        assertEquals(20, field.getWidth());
        assertEquals(15, field.getHeight());
        assertFalse(field.isGameOver());
        assertFalse(field.isGameWon());
    }

    @Test
    void testPlayerWallCollision() {
        Level startLevel = new Level(1, 100, 200);
        GameField field = new GameField(20, 15, 0, startLevel);
        field.setStarted(true);
        Snake player = field.getPlayer();

        // Move towards 0, then hit wall
        while (player.getHead().y() >= 0 && !field.isGameOver()) {
            field.update();
        }
        assertTrue(field.isGameOver());
    }

    @Test
    void testRobotWallCollision() {
        Level startLevel = new Level(1, 100, 200);
        GameField field = new GameField(20, 15, 0, startLevel);
        field.setStarted(true);

        RobotSnake robot = new RobotSnake(new Point(0, 0), Direction.LEFT, (r, f) -> Direction.LEFT);
        field.addRobot(robot);

        // Next tick it should hit left wall
        field.update();
        assertFalse(robot.isAlive());
        // Player should still be alive but game is won
        assertTrue(field.isGameWon());
    }

    @Test
    void testAddObstacle() {
        Level startLevel = new Level(1, 100, 200);
        GameField field = new GameField(20, 15, 0, startLevel);
        field.addObstacle(new Obstacle(new Point(10, 6))); // directly above player at 10,7
        field.setStarted(true);
        field.update(); // moves up to 10,6
        assertTrue(field.isGameOver());
    }
}
