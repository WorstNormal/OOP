package ru.nsu.gaev.snake.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameFieldAdditionalTest {
    @Test
    void testLevelProgression() {
        Level startLevel = new Level(1, 100, 200);
        GameField field = new GameField(10, 10, 0, startLevel);
        field.setStarted(true);
        Snake player = field.getPlayer();

        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(5, 4), FoodType.NORMAL));
        player.setNextDirection(Direction.UP);

        field.update();
        assertTrue(field.isGameWon());
    }

    @Test
    void testPlayerHitsRobot() {
        GameField field = new GameField(10, 10, 0, new Level(1, 1000, 200));
        field.setStarted(true);
        field.getPlayer().setNextDirection(Direction.RIGHT);

        RobotSnake robot = new RobotSnake(new Point(6, 5), Direction.UP, (r, f) -> Direction.UP);
        robot.eat(new Food(new Point(-1, -1), FoodType.NORMAL)); // now length 2 so tail stays
        field.addRobot(robot);

        field.update();
        assertTrue(field.isGameOver() || field.isGameDraw());
    }

    @Test
    void testRobotHitsPlayer() {
        GameField field = new GameField(10, 10, 0, new Level(1, 1000, 200));
        field.setStarted(true);

        RobotSnake robot = new RobotSnake(new Point(6, 5), Direction.LEFT, (r, f) -> Direction.LEFT);
        field.addRobot(robot);

        field.getPlayer().eat(new Food(new Point(-1, -1), FoodType.NORMAL)); // length 2
        field.getPlayer().setNextDirection(Direction.UP);

        field.update();
        // Robot hits player body
        assertTrue(field.isGameWon());
    }

    @Test
    void testRobotHitsObstacle() {
        GameField field = new GameField(10, 10, 0, new Level(1, 1000, 200));
        field.setStarted(true);

        RobotSnake robot = new RobotSnake(new Point(5, 4), Direction.UP, (r, f) -> Direction.UP);
        field.addRobot(robot);
        field.addObstacle(new Obstacle(new Point(5, 3)));

        field.update();
        assertTrue(field.isGameWon());
    }

    @Test
    void testPlayerSelfCollision() {
        GameField field = new GameField(10, 10, 0, new Level(1, 1000, 200));
        field.setStarted(true);
        Snake player = field.getPlayer();

        player.eat(new Food(new Point(-1, -1), FoodType.NORMAL));
        player.eat(new Food(new Point(-1, -1), FoodType.NORMAL));
        player.eat(new Food(new Point(-1, -1), FoodType.NORMAL));
        player.eat(new Food(new Point(-1, -1), FoodType.NORMAL));

        player.setNextDirection(Direction.LEFT);
        field.update();
        player.setNextDirection(Direction.DOWN);
        field.update();
        player.setNextDirection(Direction.RIGHT);
        field.update();
        player.setNextDirection(Direction.UP);
        field.update();

        assertTrue(field.isGameOver());
    }
}
