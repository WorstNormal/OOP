package ru.nsu.gaev.snake;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for Snake game.
 */
public class Main extends Application {
    /**
     * Main entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Start the JavaFX application.
     *
     * @param stage the primary stage
     * @throws IOException if FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("game_view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        // Ensure keyboard events are captured by the main layout
        root.requestFocus();

        stage.setTitle("Snake Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
