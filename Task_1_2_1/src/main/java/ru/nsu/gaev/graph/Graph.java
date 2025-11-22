package ru.nsu.gaev.graph;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import ru.nsu.gaev.algorithms.KahnTopologicalSortStrategy;
import ru.nsu.gaev.algorithms.TopologicalSortStrategy;

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
     * Устанавливает стратегию топологической сортировки по умолчанию для данного графа.
     *
     * @param strategy Стратегия сортировки для установки.
     */
    void setTopologicalSortStrategy(TopologicalSortStrategy<V> strategy);

    /**
     * Возвращает текущую стратегию топологической сортировки по умолчанию.
     *
     * @return Стратегия сортировки или null, если стратегия не установлена.
     */
    TopologicalSortStrategy<V> getTopologicalSortStrategy();

    /**
     * Выполняет топологическую сортировку графа с использованием стратегии по умолчанию.
     * Если стратегия не установлена, используется алгоритм Кана.
     *
     * @return Список вершин в топологическом порядке.
     * @throws IllegalStateException если в графе есть цикл.
     */
    default List<V> topologicalSort() {
        TopologicalSortStrategy<V> strategy = getTopologicalSortStrategy();
        if (strategy != null) {
            return topologicalSort(strategy);
        }
        return topologicalSort(new KahnTopologicalSortStrategy<>());
    }

    /**
     * Выполняет топологическую сортировку графа с использованием указанной стратегии.
     *
     * @param strategy Стратегия сортировки для использования.
     * @return Список вершин в топологическом порядке.
     * @throws IllegalStateException если в графе есть цикл.
     */
    default List<V> topologicalSort(TopologicalSortStrategy<V> strategy) {
        return strategy.sort(this);
    }
}