package ru.nsu.gaev.snake;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Основной класс JavaFX-приложения.
 */
public class Main extends Application {
    /**
     * Точка входа в игру.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Запускает JavaFX-приложение.
     *
     * @param stage основное окно
     * @throws IOException если не удается загрузить FXML
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("game_view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        root.requestFocus();

        stage.setTitle("Snake Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
