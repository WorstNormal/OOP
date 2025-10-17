package ru.nsu.gaev;

import java.util.*;

public class AdjacencyListGraph<V> extends AbstractGraph<V> {
    private final Map<V, Set<V>> adjList = new HashMap<>();

    @Override
    public void clear() {
        adjList.clear();
    }

    @Override
    public boolean addVertex(V vertex) {
        if (!adjList.containsKey(vertex)) {
            adjList.put(vertex, new LinkedHashSet<>());
            return super.addVertex(vertex);
        }
        return false;
    }

    @Override
    public boolean removeVertex(V vertex) {
        if (!adjList.containsKey(vertex)) return false;
        adjList.values().forEach(neighbors -> neighbors.remove(vertex));
        adjList.remove(vertex);
        vertexToIndex.remove(vertex);
        indexToVertex.remove(vertex);
        // В реальном проекте потребовалась бы переиндексация
        return true;
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
        if (!adjList.containsKey(source)) return false;
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