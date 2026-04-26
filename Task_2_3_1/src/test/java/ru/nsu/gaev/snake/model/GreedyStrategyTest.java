package ru.nsu.gaev.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.FoodType;
import ru.nsu.gaev.snake.model.common.Level;
import ru.nsu.gaev.snake.model.common.Point;
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
        Direction nextDir = strategy.chooseNextDirection(robot, field);
        assertEquals(Direction.LEFT, nextDir);

        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(5, 0), FoodType.NORMAL));
        nextDir = strategy.chooseNextDirection(robot, field);
        assertEquals(Direction.UP, nextDir);

        field.getFoods().clear();
        field.getFoods().add(new Food(new Point(5, 9), FoodType.NORMAL));
        nextDir = strategy.chooseNextDirection(robot, field);
        assertNotNull(nextDir);
    }
}
