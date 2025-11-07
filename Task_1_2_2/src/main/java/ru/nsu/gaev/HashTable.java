package ru.nsu.gaev;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HashTable<K, V> implements Iterable<Entry<K, V>> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private List<Entry<K, V>>[] table;
    private int size;
    private int capacity;
    private final float loadFactor;
    private AtomicInteger modCount = new AtomicInteger(0);

    public HashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTable(int initialCapacity, float loadFactor) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.table = (List<Entry<K, V>>[]) new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            this.table[i] = new LinkedList<>();
        }
        this.size = 0;
    }

    /**
     * Вспомогательная хеш-функция для получения индекса бакета.
     * Использует встроенный hashCode и сжимает его по емкости.
     */
    private int getIndex(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    /**
     * Добавление пары ключ-значение (k, v).
     * @return Предыдущее значение, связанное с ключом, или null, если ключ новый.
     */
    public V put(K key, V value) {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                modCount.incrementAndGet();
                return oldValue;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;
        modCount.incrementAndGet();

        if ((float) size / capacity >= loadFactor) {
            resizeAndRehash();
        }

        return null;
    }

    /**
     * Поиск значения по ключу.
     * @return Значение, связанное с ключом, или null, если ключ не найден.
     */
    public V get(K key) {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    /**
     * Удаление пары ключ-значение.
     * @return Значение, связанное с удаленным ключом, или null, если ключ не найден.
     */
    public V remove(K key) {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = table[index];

        Iterator<Entry<K, V>> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (entry.key.equals(key)) {
                V removedValue = entry.value;
                iterator.remove();
                size--;
                modCount.incrementAndGet();
                return removedValue;
            }
        }
        return null;
    }

    /**
     * Обновление значения по ключу.
     * Для простоты, делегируем к 'put', но может быть реализовано с исключением, если ключ отсутствует.
     */
    public V update(K key, V newValue) {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = newValue;
                modCount.incrementAndGet();
                return oldValue;
            }
        }
        throw new NoSuchElementException("Key not found for update operation: " + key);
    }

    /**
     * 6. Проверка наличия значения по ключу.
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    private void resizeAndRehash() {
        int oldCapacity = capacity;
        capacity *= 2;
        List<Entry<K, V>>[] newTable = new LinkedList[capacity];

        for (int i = 0; i < capacity; i++) {
            newTable[i] = new LinkedList<>();
        }


        for (int i = 0; i < oldCapacity; i++) {
            for (Entry<K, V> entry : table[i]) {
                int newIndex = getIndex(entry.key);
                newTable[newIndex].add(entry);
            }
        }

        this.table = newTable;
        modCount.incrementAndGet();
    }



    /**
     * Итерирование по элементам коллекции с обработкой ConcurrentModificationException.
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator();
    }

    private class HashTableIterator implements Iterator<Entry<K, V>> {
        private final int expectedModCount = modCount.get();
        private int currentBucket = 0;
        private Iterator<Entry<K, V>> currentIterator = null;
        private Entry<K, V> lastReturned = null;

        @Override
        public boolean hasNext() {
            checkForComodification();
            while ((currentIterator == null || !currentIterator.hasNext()) && currentBucket < capacity) {
                if (!table[currentBucket].isEmpty()) {
                    currentIterator = table[currentBucket].iterator();
                }
                currentBucket++;
            }
            return currentIterator != null && currentIterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = currentIterator.next();
            return lastReturned;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            checkForComodification();
            currentIterator.remove();
            size--;
            modCount.incrementAndGet();
        }

        private void checkForComodification() {
            if (modCount.get() != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }


    /**
     * Сравнение на равенство с другой хеш-таблицей.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashTable<Object, Object> that = (HashTable<Object, Object>) o;

        if (this.size != that.size) return false;
        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                Object otherValue = that.get(entry.key);
                if (otherValue == null || !entry.value.equals(otherValue)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                h += entry.key.hashCode() ^ entry.value.hashCode();
            }
        }
        return h;
    }


    /**
     * Вывод в строку.
     */
    @Override
    public String toString() {
        if (size == 0) return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;

        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(entry.key).append("=").append(entry.value);
                first = false;
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
