package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Реализация топологической сортировки с использованием алгоритма DFS (Depth-First Search).
 * Алгоритм использует поиск в глубину с временными метками.
 */
public class DFSTopologicalSortStrategy<V> implements TopologicalSortStrategy<V> {

    @Override
    public List<V> sort(Graph<V> graph) {
        Set<V> visited = new HashSet<>();
        Set<V> recursionStack = new HashSet<>();
        LinkedList<V> result = new LinkedList<>();

        for (V vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                if (!dfsVisit(graph, vertex, visited, recursionStack, result)) {
                    throw new IllegalStateException("Graph has a cycle, topological sort is not possible.");
                }
            }
        }

        return new ArrayList<>(result);
    }

    private boolean dfsVisit(Graph<V> graph, V vertex, Set<V> visited, Set<V> recursionStack, LinkedList<V> result) {
        if (recursionStack.contains(vertex)) {
            return false; // Найден цикл
        }

        if (visited.contains(vertex)) {
            return true; // Уже обработан
        }

        visited.add(vertex);
        recursionStack.add(vertex);

        for (V neighbor : graph.getNeighbors(vertex)) {
            if (!dfsVisit(graph, neighbor, visited, recursionStack, result)) {
                return false;
            }
        }

        recursionStack.remove(vertex);
        result.addFirst(vertex); // Добавляем в начало списка (обратный порядок)

        return true;
    }
}

