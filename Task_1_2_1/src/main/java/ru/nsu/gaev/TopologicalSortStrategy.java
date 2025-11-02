package ru.nsu.gaev;

import java.util.List;

/**
 * Интерфейс для стратегий топологической сортировки графа.
 *
 * @param <V> Тип данных, хранящихся в вершинах графа.
 */
public interface TopologicalSortStrategy<V> {
    /**
     * Выполняет топологическую сортировку графа.
     *
     * @param graph Граф для сортировки.
     * @return Список вершин в топологическом порядке.
     * @throws IllegalStateException если в графе есть цикл.
     */
    List<V> sort(Graph<V> graph);
}

