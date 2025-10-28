package ru.nsu.gaev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Интерфейс, представляющий структуру данных "Граф".
 *
 * @param <V> Тип данных, хранящихся в вершинах.
 */
public interface Graph<V> {

    void clear();

    /**
     * Добавляет вершину в граф.
     *
     * @param vertex Вершина для добавления.
     * @return true, если вершина была успешно добавлена.
     */
    boolean addVertex(V vertex);

    /**
     * Удаляет вершину из графа.
     *
     * @param vertex Вершина для удаления.
     * @return true, если вершина была успешно удалена.
     */
    boolean removeVertex(V vertex);

    /**
     * Добавляет направленное ребро между двумя вершинами.
     *
     * @param source Начальная вершина.
     * @param destination Конечная вершина.
     * @return true, если ребро было успешно добавлено.
     */
    boolean addEdge(V source, V destination);

    /**
     * Удаляет ребро между двумя вершинами.
     *
     * @param source Начальная вершина.
     * @param destination Конечная вершина.
     * @return true, если ребро было успешно удалено.
     */
    boolean removeEdge(V source, V destination);

    /**
     * Возвращает множество всех "соседей" (смежных вершин) для данной вершины.
     *
     * @param vertex Вершина, для которой ищутся соседи.
     * @return Множество смежных вершин.
     */
    Set<V> getNeighbors(V vertex);

    /**
     * Возвращает множество всех вершин в графе.
     *
     * @return Множество вершин.
     */
    Set<V> getVertices();

    /**
     * Проверяет наличие ребра между двумя вершинами.
     *
     * @param source Начальная вершина.
     * @param destination Конечная вершина.
     * @return true, если ребро существует.
     */
    boolean hasEdge(V source, V destination);

    /**
     * Считывает граф из файла определенного формата.
     *
     * @param filePath Путь к файлу.
     * @throws IOException если возникает ошибка чтения.
     */
    void readFromFile(String filePath) throws IOException;

    /**
     * Выполняет топологическую сортировку графа.
     *
     * @return Список вершин в топологическом порядке.
     * @throws IllegalStateException если в графе есть цикл.
     */
    default List<V> topologicalSort() {
        Map<V, Integer> inDegree = new HashMap<>();
        for (V vertex : this.getVertices()) {
            inDegree.put(vertex, 0);
        }

        for (V vertex : this.getVertices()) {
            for (V neighbor : this.getNeighbors(vertex)) {
                // Обработка случая, когда neighbor может не быть в inDegree
                // (хотя по логике getVertices должен)
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

            for (V neighbor : this.getNeighbors(vertex)) {
                int newDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newDegree);
                if (newDegree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sortedOrder.size() != this.getVertices().size()) {
            throw new IllegalStateException("Graph has a cycle, topological sort is not possible.");
        }

        return sortedOrder;
    }
}