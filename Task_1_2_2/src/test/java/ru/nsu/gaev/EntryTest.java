package ru.nsu.gaev;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {

    @Test
    @DisplayName("Test Entry creation and field access")
    void testEntryCreation() {
        Entry<String, Integer> entry = new Entry<>("key", 100);

        assertEquals("key", entry.key);
        assertEquals(100, entry.value);
    }

    @Test
    @DisplayName("Test equals() method logic")
    void testEquals() {
        Entry<String, Integer> entry1 = new Entry<>("test", 1);
        Entry<String, Integer> entry2 = new Entry<>("test", 1);
        Entry<String, Integer> entry3 = new Entry<>("other", 1);
        Entry<String, Integer> entry4 = new Entry<>("test", 2);

        // Рефлексивность
        assertEquals(entry1, entry1);

        // Симметричность для одинаковых данных
        assertEquals(entry1, entry2);
        assertEquals(entry2, entry1);

        // Неравенство
        assertNotEquals(entry1, entry3); // Разные ключи
        assertNotEquals(entry1, entry4); // Разные значения
        assertNotEquals(entry1, null);   // Null check
        assertNotEquals(entry1, "String object"); // Другой класс
    }

    @Test
    @DisplayName("Test hashCode() contract")
    void testHashCode() {
        Entry<String, Integer> entry1 = new Entry<>("test", 1);
        Entry<String, Integer> entry2 = new Entry<>("test", 1);

        // Если объекты равны по equals, их хеш-коды должны совпадать
        assertEquals(entry1.hashCode(), entry2.hashCode());

        // Проверка формулы хеш-кода (XOR ключа и значения)
        int expectedHash = "test".hashCode() ^ Integer.valueOf(1).hashCode();
        assertEquals(expectedHash, entry1.hashCode());
    }

    @Test
    @DisplayName("Test toString() output format")
    void testToString() {
        Entry<String, Integer> entry = new Entry<>("key", 123);
        assertEquals("key=123", entry.toString());
    }
}