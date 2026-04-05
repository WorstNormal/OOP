package ru.nsu.gaev.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for JavaFX Main class.
 */
class MainTest {
    private static volatile boolean javaFxSupported = true;

    @BeforeAll
    static void initJavaFxToolkit() {
        try {
            Platform.startup(() -> {
                // Toolkit init.
            });
        } catch (IllegalStateException ignored) {
            // JavaFX runtime already initialized by another test.
        } catch (UnsupportedOperationException ignored) {
            javaFxSupported = false;
        }
    }

    @Test
    void testStartConfiguresAndShowsStage() throws InterruptedException {
        Assumptions.assumeTrue(javaFxSupported,
                "JavaFX toolkit is not supported in current environment");

        Main app = new Main();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                app.start(stage);

                assertNotNull(stage.getScene());
                assertEquals("Snake Game", stage.getTitle());
                assertFalse(stage.isResizable());
                assertTrue(stage.isShowing());

                stage.close();
            } catch (Throwable t) {
                failure.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        if (failure.get() != null) {
            throw new AssertionError("JavaFX stage startup failed", failure.get());
        }
    }
}
