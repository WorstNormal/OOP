package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

class PizzeriaConfigTest {

    @Test
    void testConfigDeserialization() {
        String json = """
            {
                "bakers": [
                    {"id": 1, "cookingTimeMs": 100},
                    {"id": 2, "cookingTimeMs": 150}
                ],
                "couriers": [
                    {"id": 1, "trunkCapacity": 2, "deliveryTimeMs": 200},
                    {"id": 2, "trunkCapacity": 3, "deliveryTimeMs": 250}
                ],
                "storageCapacity": 10,
                "workingTimeMs": 5000
            }
            """;

        Gson gson = new Gson();
        PizzeriaConfig config = gson.fromJson(json, PizzeriaConfig.class);

        assertNotNull(config);
        assertEquals(10, config.getStorageCapacity());
        assertEquals(5000, config.getWorkingTimeMs());

        // Проверяем пекарей
        assertEquals(2, config.getBakers().length);
        assertEquals(1, config.getBakers()[0].getId());
        assertEquals(100, config.getBakers()[0].getCookingTimeMs());
        assertEquals(2, config.getBakers()[1].getId());
        assertEquals(150, config.getBakers()[1].getCookingTimeMs());

        // Проверяем курьеров
        assertEquals(2, config.getCouriers().length);
        assertEquals(1, config.getCouriers()[0].getId());
        assertEquals(2, config.getCouriers()[0].getTrunkCapacity());
        assertEquals(200, config.getCouriers()[0].getDeliveryTimeMs());
        assertEquals(2, config.getCouriers()[1].getId());
        assertEquals(3, config.getCouriers()[1].getTrunkCapacity());
        assertEquals(250, config.getCouriers()[1].getDeliveryTimeMs());
    }
}

