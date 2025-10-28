package ru.nsu.gaev;

import java.util.*;

/**
 * Абстрактный класс для реализаций графа, основанных на индексах (матрицы).
 * Он инкапсулирует логику отображения Vertex <-> Integer.
 */
public abstract class AbstractIndexBasedGraph<V> extends AbstractGraph<V> {

    protected final Map<V, Integer> vertexToIndex = new HashMap<>();
    protected final List<V> indexToVertex = new ArrayList<>();

    /**
     * Очищает карты индексов.
     * Наследники должны вызывать super.clear()
     */
    @Override
    public void clear() {
        vertexToIndex.clear();
        indexToVertex.clear();
    }

    @Override
    public Set<V> getVertices() {
        return Collections.unmodifiableSet(vertexToIndex.keySet());
    }

    @Override
    public boolean addVertex(V vertex) {
        if (vertexToIndex.containsKey(vertex)) {
            return false;
        }
        vertexToIndex.put(vertex, indexToVertex.size());
        indexToVertex.add(vertex);
        return true;
    }
}