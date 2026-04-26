package ru.nsu.gaev.snake.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.core.GameFieldView;
import ru.nsu.gaev.snake.model.entity.Food;
import ru.nsu.gaev.snake.model.entity.Obstacle;
import ru.nsu.gaev.snake.model.entity.RobotSnake;
import ru.nsu.gaev.snake.model.entity.Snake;

/**
 * Отрисовывает текущее состояние игрового поля.
 */
public class GameRenderer {
    private static final String START_MESSAGE = "Press any arrow key to start";
    private static final int START_MESSAGE_FONT_SIZE = 24;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final int cellSize;

    /**
     * Создает рендерер.
     *
     * @param canvas холст для рисования
     * @param cellSize размер клетки
     */
    public GameRenderer(Canvas canvas, int cellSize) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.cellSize = cellSize;
    }

    public int getFieldWidth() {
        return (int) (canvas.getWidth() / cellSize);
    }

    public int getFieldHeight() {
        return (int) (canvas.getHeight() / cellSize);
    }

    /**
     * Рисует игровое состояние.
     *
     * @param field игровое поле
     */
    public void render(GameFieldView field) {
        gc.setFill(Color.web("#eef2f3"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.LIGHTGRAY);
        for (int i = 0; i < field.getWidth(); i++) {
            gc.strokeLine(i * cellSize, 0, i * cellSize, canvas.getHeight());
        }
        for (int i = 0; i < field.getHeight(); i++) {
            gc.strokeLine(0, i * cellSize, canvas.getWidth(), i * cellSize);
        }

        gc.setFill(Color.RED);
        for (Food food : field.getFoods()) {
            Point p = food.position();
            gc.fillOval(p.x() * cellSize + 2, p.y() * cellSize + 2, cellSize - 4, cellSize - 4);
        }

        gc.setFill(Color.DARKGRAY);
        for (Obstacle obs : field.getObstacles()) {
            Point p = obs.position();
            gc.fillRect(p.x() * cellSize, p.y() * cellSize, cellSize, cellSize);
        }

        if (field.getPlayer().isAlive()) {
            drawSnake(field.getPlayer(), Color.DARKGREEN, Color.LIMEGREEN);
        } else {
            drawSnake(field.getPlayer(), Color.DARKGRAY, Color.GRAY);
        }

        for (RobotSnake robot : field.getRobots()) {
            if (robot.isAlive()) {
                drawSnake(robot, Color.DARKBLUE, Color.BLUE);
            } else {
                drawSnake(robot, Color.DARKGRAY, Color.GRAY);
            }
        }

        if (!field.isStarted() && !field.isGameOver() && !field.isGameWon()) {
            gc.setFill(Color.BLACK);
            gc.setFont(new javafx.scene.text.Font("Arial", START_MESSAGE_FONT_SIZE));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(START_MESSAGE, canvas.getWidth() / 2, canvas.getHeight() / 2);
        }
    }

    private void drawSnake(Snake snake, Color headColor, Color bodyColor) {
        boolean isHead = true;
        for (Point p : snake.getBody()) {
            gc.setFill(isHead ? headColor : bodyColor);
            gc.fillRect(p.x() * cellSize + 1, p.y() * cellSize + 1, cellSize - 2, cellSize - 2);
            isHead = false;
        }
    }
}
