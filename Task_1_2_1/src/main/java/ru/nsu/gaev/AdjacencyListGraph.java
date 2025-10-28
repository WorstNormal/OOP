package ru.nsu.gaev;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Реализация графа через список смежности.
 *
 * @param <V> тип вершины
 */
public class AdjacencyListGraph<V> extends AbstractGraph<V> {
    private final Map<V, Set<V>> adjList = new HashMap<>();

    /**
     * Реализуем clear() из AbstractGraph.
     */
    @Override
    public void clear() {
        adjList.clear();
    }

    /**
     * Реализуем addVertex() из AbstractGraph.
     * Логика не использует индексы.
     */
    @Override
    public boolean addVertex(V vertex) {
        if (adjList.containsKey(vertex)) {
            return false;
        }
        adjList.put(vertex, new LinkedHashSet<>());
        return true;
    }

    /**
     * Реализуем removeVertex() из AbstractGraph.
     * Логика не использует индексы.
     */
    @Override
    public boolean removeVertex(V vertex) {
        if (!adjList.containsKey(vertex)) {
            return false;
        }
        adjList.values().forEach(neighbors -> neighbors.remove(vertex));
        adjList.remove(vertex);
        return true;
    }

    /**
     * Реализуем getVertices() из AbstractGraph.
     */
    @Override
    public Set<V> getVertices() {
        return Collections.unmodifiableSet(adjList.keySet());
    }

    @Override
    public boolean addEdge(V source, V destination) {
        if (!adjList.containsKey(source) || !adjList.containsKey(destination)) {
            throw new IllegalArgumentException("Source or destination vertex not found.");
        }
        return adjList.get(source).add(destination);
    }

    @Override
    public boolean removeEdge(V source, V destination) {
        if (!adjList.containsKey(source)) {
            return false;
        }
        return adjList.get(source).remove(destination);
    }

    @Override
    public Set<V> getNeighbors(V vertex) {
        return Collections.unmodifiableSet(adjList.getOrDefault(vertex, Collections.emptySet()));
    }

    @Override
    public boolean hasEdge(V source, V destination) {
        return adjList.containsKey(source) && adjList.get(source).contains(destination);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Adjacency List Graph:\n");
        for (Map.Entry<V, Set<V>> entry : adjList.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}