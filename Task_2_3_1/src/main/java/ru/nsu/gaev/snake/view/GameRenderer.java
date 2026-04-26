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
 * Отрисовывает текущее состояни�� игрового поля.
 */
public class GameRenderer {
    // Размер одной клетки в пикселях
    private static final int CELL_SIZE = 30;

    // Сообщение при старте игры
    private static final String START_MESSAGE = "Press any arrow key to start";
    private static final int START_MESSAGE_FONT_SIZE = 24;

    // Смещения и размеры для рисования еды (овала)
    private static final int FOOD_OVAL_OFFSET = 2;
    private static final int FOOD_OVAL_SIZE_REDUCTION = 4;

    // Смещения и размеры для рисования тела змеи (прямоугольника)
    private static final int SNAKE_RECT_OFFSET = 1;
    private static final int SNAKE_RECT_SIZE_REDUCTION = 2;

    // Смещение для текста стартового сообщения от центра
    private static final int TEXT_Y_OFFSET = 0;

    private final Canvas canvas;
    private final GraphicsContext gc;

    /**
     * Создает рендерер.
     *
     * @param canvas холст для рисования
     */
    public GameRenderer(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public int getFieldWidth() {
        return (int) (canvas.getWidth() / CELL_SIZE);
    }

    public int getFieldHeight() {
        return (int) (canvas.getHeight() / CELL_SIZE);
    }

    /**
     * Рисует игровое состояние.
     *
     * @param field игровое поле
     */
    public void render(GameFieldView field) {
        drawBackground();
        drawGrid(field);
        drawFood(field);
        drawObstacles(field);
        drawSnakes(field);
        drawStartMessage(field);
    }

    private void drawBackground() {
        gc.setFill(Color.web("#eef2f3"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawGrid(GameFieldView field) {
        gc.setStroke(Color.LIGHTGRAY);
        for (int i = 0; i < field.getWidth(); i++) {
            gc.strokeLine(i * CELL_SIZE, 0, i * CELL_SIZE, canvas.getHeight());
        }
        for (int i = 0; i < field.getHeight(); i++) {
            gc.strokeLine(0, i * CELL_SIZE, canvas.getWidth(), i * CELL_SIZE);
        }
    }

    private void drawFood(GameFieldView field) {
        gc.setFill(Color.RED);
        for (Food food : field.getFoods()) {
            Point p = food.position();
            gc.fillOval(
                    p.x() * CELL_SIZE + FOOD_OVAL_OFFSET,
                    p.y() * CELL_SIZE + FOOD_OVAL_OFFSET,
                    CELL_SIZE - FOOD_OVAL_SIZE_REDUCTION,
                    CELL_SIZE - FOOD_OVAL_SIZE_REDUCTION
            );
        }
    }

    private void drawObstacles(GameFieldView field) {
        gc.setFill(Color.DARKGRAY);
        for (Obstacle obs : field.getObstacles()) {
            Point p = obs.position();
            gc.fillRect(p.x() * CELL_SIZE, p.y() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawSnakes(GameFieldView field) {
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
    }

    private void drawStartMessage(GameFieldView field) {
        if (!field.isStarted() && !field.isGameOver() && !field.isGameWon()) {
            gc.setFill(Color.BLACK);
            gc.setFont(new javafx.scene.text.Font("Arial", START_MESSAGE_FONT_SIZE));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(
                    START_MESSAGE,
                    canvas.getWidth() / 2,
                    canvas.getHeight() / 2 + TEXT_Y_OFFSET
            );
        }
    }

    private void drawSnake(Snake snake, Color headColor, Color bodyColor) {
        boolean isHead = true;
        for (Point p : snake.getBody()) {
            gc.setFill(isHead ? headColor : bodyColor);
            gc.fillRect(
                    p.x() * CELL_SIZE + SNAKE_RECT_OFFSET,
                    p.y() * CELL_SIZE + SNAKE_RECT_OFFSET,
                    CELL_SIZE - SNAKE_RECT_SIZE_REDUCTION,
                    CELL_SIZE - SNAKE_RECT_SIZE_REDUCTION
            );
            isHead = false;
        }
    }
}
