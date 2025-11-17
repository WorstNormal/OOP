package ru.nsu.gaev;

class Entry<K, V> {
    K key;
    V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entry<?, ?> entry = (Entry<?, ?>) o;
        return key.equals(entry.key) && value.equals(entry.value);
    }

    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}