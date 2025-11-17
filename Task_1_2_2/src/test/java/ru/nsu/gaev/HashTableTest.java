package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashTableTest {

    private HashTable<String, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>();
    }

    @Test
    @DisplayName("Put adds element and Get retrieves it")
    void testPutAndGet() {
        assertNull(hashTable.put("one", 1)); // Новый ключ возвращает null
        assertEquals(1, hashTable.get("one"));

        // Обновление существующего ключа возвращает старое значение
        assertEquals(1, hashTable.put("one", 2));
        assertEquals(2, hashTable.get("one"));
    }

    @Test
    @DisplayName("Get returns null for missing keys")
    void testGetMissing() {
        assertNull(hashTable.get("missing_key"));
    }

    @Test
    @DisplayName("Update modifies existing value correctly")
    void testUpdateSuccess() {
        hashTable.put("key", 10);
        Integer oldValue = hashTable.update("key", 20);

        assertEquals(10, oldValue);
        assertEquals(20, hashTable.get("key"));
    }

    @Test
    @DisplayName("Update throws exception for non-existent key")
    void testUpdateThrowsException() {
        assertThrows(NoSuchElementException.class, () -> {
            hashTable.update("non_existent", 5);
        });
    }

    @Test
    @DisplayName("Remove deletes entry and returns value")
    void testRemove() {
        hashTable.put("key", 100);
        assertTrue(hashTable.containsKey("key"));

        Integer removed = hashTable.remove("key");
        assertEquals(100, removed);
        assertFalse(hashTable.containsKey("key"));
        assertNull(hashTable.get("key"));

        // Удаление несуществующего
        assertNull(hashTable.remove("key"));
    }

    @Test
    @DisplayName("ContainsKey checks existence correctly")
    void testContainsKey() {
        hashTable.put("A", 1);
        assertTrue(hashTable.containsKey("A"));
        assertFalse(hashTable.containsKey("B"));
    }

    @Test
    @DisplayName("Null keys are not allowed")
    void testNullKeys() {
        assertThrows(IllegalArgumentException.class, () -> hashTable.put(null, 1));
        assertThrows(IllegalArgumentException.class, () -> hashTable.get(null));
        assertThrows(IllegalArgumentException.class, () -> hashTable.remove(null));
    }

    @Test
    @DisplayName("Resize and Rehash handles capacity expansion")
    void testResize() {
        HashTable<String, Integer> smallTable = new HashTable<>(2, 0.75f);

        smallTable.put("1", 1);
        smallTable.put("2", 2);
        smallTable.put("3", 3);
        smallTable.put("4", 4);

        assertEquals(1, smallTable.get("1"));
        assertEquals(2, smallTable.get("2"));
        assertEquals(3, smallTable.get("3"));
        assertEquals(4, smallTable.get("4"));
    }

    @Test
    @DisplayName("Iterator traverses all elements")
    void testIterator() {
        hashTable.put("A", 1);
        hashTable.put("B", 2);

        int count = 0;
        for (Entry<String, Integer> entry : hashTable) {
            count++;
            assertNotNull(entry.key);
        }
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Iterator throws ConcurrentModificationException on external modification")
    void testConcurrentModification() {
        hashTable.put("A", 1);

        assertThrows(ConcurrentModificationException.class, () -> {
            for (Entry<String, Integer> entry : hashTable) {
                hashTable.put("B", 2);
            }
        });
    }

    @Test
    @DisplayName("Iterator remove() works correctly")
    void testIteratorRemove() {
        hashTable.put("A", 1);
        hashTable.put("B", 2);

        Iterator<Entry<String, Integer>> it = hashTable.iterator();
        while (it.hasNext()) {
            Entry<String, Integer> entry = it.next();
            if (entry.key.equals("A")) {
                it.remove();
            }
        }

        assertFalse(hashTable.containsKey("A"));
        assertTrue(hashTable.containsKey("B"));
        // Разбиваем длинную строку для CheckStyle
        int size = hashTable.toString().split(",").length < 2 ? 1 : 0;
        assertEquals(1, size);
    }

    @Test
    @DisplayName("HashTable Equals comparison")
    void testEquals() {
        HashTable<String, Integer> table1 = new HashTable<>();
        table1.put("a", 1);
        table1.put("b", 2);

        HashTable<String, Integer> table2 = new HashTable<>();
        table2.put("b", 2);
        table2.put("a", 1);

        HashTable<String, Integer> table3 = new HashTable<>();
        table3.put("a", 1);
        table3.put("b", 3);

        assertEquals(table1, table2);
        assertNotEquals(table1, table3);
    }
}