package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Реализация топологической сортировки с использованием алгоритма Кана (Kahn's algorithm).
 * Алгоритм использует подсчет входящих степеней вершин.
 */
public class KahnTopologicalSortStrategy<V> implements TopologicalSortStrategy<V> {

    @Override
    public List<V> sort(Graph<V> graph) {
        Map<V, Integer> inDegree = new HashMap<>();
        for (V vertex : graph.getVertices()) {
            inDegree.put(vertex, 0);
        }

        for (V vertex : graph.getVertices()) {
            for (V neighbor : graph.getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        Queue<V> queue = new LinkedList<>();
        for (Map.Entry<V, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<V> sortedOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            V vertex = queue.poll();
            sortedOrder.add(vertex);

            for (V neighbor : graph.getNeighbors(vertex)) {
                int newDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newDegree);
                if (newDegree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sortedOrder.size() != graph.getVertices().size()) {
            throw new IllegalStateException("Graph has a cycle, topological sort is not possible.");
        }

        return sortedOrder;
    }
}

