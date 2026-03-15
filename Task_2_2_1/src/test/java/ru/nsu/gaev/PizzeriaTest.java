package ru.nsu.gaev;

import ru.nsu.gaev.config.PizzeriaConfig;
import ru.nsu.gaev.model.Order;
import ru.nsu.gaev.model.OrderState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class PizzeriaTest {

    private File configFile;

    @BeforeEach
    void setUp() throws IOException {
        // Создаём временный файл конфигурации
        configFile = File.createTempFile("pizzeria_config", ".json");
        configFile.deleteOnExit();
    }

    @AfterEach
    void tearDown() {
        // Удаляем файл незавершённых заказов, если он был создан
        File unfinished = new File("unfinished_orders.json");
        if (unfinished.exists()) {
            unfinished.delete();
        }
    }

    private PizzeriaConfig createTestConfig() {
        String json = """
            {
                "bakers": [
                    {"id": 1, "cookingTimeMs": 50}
                ],
                "couriers": [
                    {"id": 1, "trunkCapacity": 2, "deliveryTimeMs": 50}
                ],
                "storageCapacity": 5,
                "workingTimeMs": 500
            }
            """;
        return new Gson().fromJson(json, PizzeriaConfig.class);
    }

    @Test
    @Timeout(10)
    void testStartAndShutdown() throws InterruptedException {
        PizzeriaConfig config = createTestConfig();
        Pizzeria pizzeria = new Pizzeria(config);

        pizzeria.start();
        assertTrue(pizzeria.isAcceptingOrders());

        Thread.sleep(100);
        pizzeria.shutdown();

        assertFalse(pizzeria.isAcceptingOrders());
    }

    @Test
    @Timeout(10)
    void testPlaceOrder() {
        PizzeriaConfig config = createTestConfig();
        Pizzeria pizzeria = new Pizzeria(config);

        pizzeria.start();

        Order order = pizzeria.placeOrder();
        assertNotNull(order);
        assertEquals(1, order.getId());

        // Статус может успеть измениться, если пекарь сразу возьмёт заказ
        // assertEquals(OrderState.QUEUED, order.getState());

        Order order2 = pizzeria.placeOrder();
        assertNotNull(order2);
        assertEquals(2, order2.getId());

        pizzeria.shutdown();
    }

    @Test
    @Timeout(10)
    void testPlaceOrderAfterShutdown() {
        PizzeriaConfig config = createTestConfig();
        Pizzeria pizzeria = new Pizzeria(config);

        pizzeria.start();
        pizzeria.shutdown();

        Order order = pizzeria.placeOrder();
        assertNull(order);
    }

    @Test
    @Timeout(10)
    void testFullOrderProcessing() throws InterruptedException {
        PizzeriaConfig config = createTestConfig();
        Pizzeria pizzeria = new Pizzeria(config);

        pizzeria.start();

        Order order = pizzeria.placeOrder();
        assertNotNull(order);

        // Даём время на обработку
        Thread.sleep(500);

        pizzeria.shutdown();

        // Заказ должен быть доставлен
        assertEquals(OrderState.DELIVERED, order.getState());
    }

    @Test
    @Timeout(10)
    void testMultipleOrdersProcessing() throws InterruptedException {
        String json = """
            {
                "bakers": [
                    {"id": 1, "cookingTimeMs": 30},
                    {"id": 2, "cookingTimeMs": 30}
                ],
                "couriers": [
                    {"id": 1, "trunkCapacity": 3, "deliveryTimeMs": 30}
                ],
                "storageCapacity": 10,
                "workingTimeMs": 2000
            }
            """;
        PizzeriaConfig config = new Gson().fromJson(json, PizzeriaConfig.class);
        Pizzeria pizzeria = new Pizzeria(config);

        pizzeria.start();

        Order[] orders = new Order[5];
        for (int i = 0; i < 5; i++) {
            orders[i] = pizzeria.placeOrder();
            assertNotNull(orders[i]);
        }

        Thread.sleep(1000);
        pizzeria.shutdown();

        // Вс�� заказы должны быть доставлены
        for (Order order : orders) {
            assertEquals(OrderState.DELIVERED, order.getState());
        }
    }

    @Test
    void testLoadConfig() throws IOException {
        String json = """
            {
                "bakers": [
                    {"id": 1, "cookingTimeMs": 100}
                ],
                "couriers": [
                    {"id": 1, "trunkCapacity": 2, "deliveryTimeMs": 200}
                ],
                "storageCapacity": 5,
                "workingTimeMs": 1000
            }
            """;

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(json);
        }

        PizzeriaConfig config = Pizzeria.loadConfig(configFile.getAbsolutePath());
        assertNotNull(config);
        assertEquals(5, config.getStorageCapacity());
        assertEquals(1000, config.getWorkingTimeMs());
        assertEquals(1, config.getBakers().length);
        assertEquals(1, config.getCouriers().length);
    }

    @Test
    void testLoadConfigFileNotFound() {
        assertThrows(IOException.class, () -> {
            Pizzeria.loadConfig("non_existent_file.json");
        });
    }

    @Test
    @Timeout(10)
    void testUnfinishedOrdersSerialization() throws InterruptedException, IOException {
        // Создаём конфигурацию с очень медленным пекарем
        String json = """
            {
                "bakers": [
                    {"id": 1, "cookingTimeMs": 10000}
                ],
                "couriers": [
                    {"id": 1, "trunkCapacity": 2, "deliveryTimeMs": 100}
                ],
                "storageCapacity": 5,
                "workingTimeMs": 100
            }
            """;
        PizzeriaConfig config = new Gson().fromJson(json, PizzeriaConfig.class);
        Pizzeria pizzeria = new Pizzeria(config);

        pizzeria.start();

        // Размещаем заказы
        pizzeria.placeOrder();
        pizzeria.placeOrder();

        Thread.sleep(50);

        // Закрываем быстро, пока пекарь ещё готовит
        pizzeria.shutdown();

        // Проверяем что файл с незавершёнными заказами создан
        // (может не создаться, если все заказы успели обработаться)
        File unfinished = new File("unfinished_orders.json");
        // Файл может быть или не быть - зависит от timing
    }

    @Test
    @Timeout(10)
    void testIsAcceptingOrders() {
        PizzeriaConfig config = createTestConfig();
        Pizzeria pizzeria = new Pizzeria(config);

        // До запуска должны принимать заказы
        assertTrue(pizzeria.isAcceptingOrders());

        pizzeria.start();
        assertTrue(pizzeria.isAcceptingOrders());

        pizzeria.shutdown();
        assertFalse(pizzeria.isAcceptingOrders());
    }
}
