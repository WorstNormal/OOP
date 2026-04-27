package ru.nsu.gaev.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.FoodType;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.entity.Food;
import ru.nsu.gaev.snake.model.entity.Snake;

class SnakeTest {
    @Test
    void testInitialState() {
        Snake snake = new Snake(new Point(5, 5), Direction.UP);
        assertTrue(snake.isAlive());
        assertEquals(Direction.UP, snake.getCurrentDirection());
        assertEquals(1, snake.getBody().size());
        assertEquals(new Point(5, 5), snake.getHead());
    }

    @Test
    void testMove() {
        Snake snake = new Snake(new Point(5, 5), Direction.UP);
        snake.move();
        assertEquals(new Point(5, 4), snake.getHead());
        assertEquals(1, snake.getBody().size());
    }

    @Test
    void testGrow() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.eat(new Food(new Point(6, 5), FoodType.NORMAL));
        snake.move();
        assertEquals(2, snake.getBody().size());
        assertEquals(new Point(6, 5), snake.getHead());
        assertEquals(new Point(5, 5), snake.getBody().get(1));
    }

    @Test
    void testSelfCollision() {
        Snake snake = new Snake(new Point(5, 5), Direction.UP);
        snake.eat(new Food(new Point(0, 0), FoodType.NORMAL));
        snake.eat(new Food(new Point(0, 0), FoodType.NORMAL));
        snake.eat(new Food(new Point(0, 0), FoodType.NORMAL));
        snake.eat(new Food(new Point(0, 0), FoodType.NORMAL));
        snake.eat(new Food(new Point(0, 0), FoodType.NORMAL));

        snake.setNextDirection(Direction.RIGHT);
        snake.move();
        snake.setNextDirection(Direction.DOWN);
        snake.move();
        snake.setNextDirection(Direction.LEFT);
        snake.move();
        snake.setNextDirection(Direction.UP);
        snake.move();

        assertTrue(snake.checkSelfCollision());
    }

    @Test
    void testOccupies() {
        Snake snake = new Snake(new Point(5, 5), Direction.UP);
        assertTrue(snake.occupies(new Point(5, 5)));
        assertFalse(snake.occupies(new Point(4, 5)));
    }
}
