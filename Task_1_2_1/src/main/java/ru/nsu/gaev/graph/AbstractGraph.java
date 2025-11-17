package ru.nsu.gaev.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import ru.nsu.gaev.algorithms.TopologicalSortStrategy;

/**
 * Абстрактный базовый класс для всех реализаций графа.
 * Содержит общую логику чтения из файла и реализации equals/hashCode.
 *
 * @param <V> тип вершины
 */
public abstract class AbstractGraph<V> implements Graph<V> {

    /**
     * Стратегия топологической сортировки по умолчанию для данного графа.
     * Может быть установлена через метод setTopologicalSortStrategy().
     */
    private TopologicalSortStrategy<V> topologicalSortStrategy;

    /**
     * Очищает все внутренние структуры графа (ребра и т.д.),
     * подготавливая его к чтению новых данных из файла.
     */
    public abstract void clear();

    @Override
    public abstract Set<V> getVertices();

    @Override
    public abstract boolean addVertex(V vertex);

    @Override
    public abstract boolean removeVertex(V vertex);

    @Override
    public void readFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            this.clear();

            String line;

            line = reader.readLine();
            while (line != null && line.trim().isEmpty()) {
                line = reader.readLine();
            }
            if (line == null) {
                throw new IOException("File is empty or invalid: missing vertex count.");
            }
            int numVertices = Integer.parseInt(line.trim());

            for (int i = 0; i < numVertices; i++) {
                line = reader.readLine();
                if (line == null) {
                    throw new IOException("Unexpected end of file while reading vertices.");
                }
                if (line.trim().isEmpty()) {
                    i--;
                    continue;
                }
                @SuppressWarnings("unchecked")
                V vertex = (V) line.trim();
                this.addVertex(vertex);
            }

            line = reader.readLine();
            while (line != null && line.trim().isEmpty()) {
                line = reader.readLine();
            }
            if (line == null) {
                return;
            }
            int numEdges = Integer.parseInt(line.trim());

            for (int i = 0; i < numEdges; i++) {
                line = reader.readLine();
                if (line == null) {
                    throw new IOException("Unexpected end of file while reading edges.");
                }
                if (line.trim().isEmpty()) {
                    i--;
                    continue;
                }

                String[] edgeParts = line.trim().split("\\s+");
                if (edgeParts.length < 2) {
                    throw new IOException("Invalid edge format: " + line);
                }

                @SuppressWarnings("unchecked")
                V source = (V) edgeParts[0];
                @SuppressWarnings("unchecked")
                V dest = (V) edgeParts[1];
                this.addEdge(source, dest);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Graph<?>)) {
            return false;
        }
        Graph<?> other = (Graph<?>) o;


        if (!this.getVertices().equals(other.getVertices())) {
            return false;
        }

        for (V vertex : this.getVertices()) {
            Set<V> thisNeighbors = this.getNeighbors(vertex);
            if (!other.getVertices().contains(vertex)) {
                return false;
            }

            @SuppressWarnings("unchecked")
            Set<V> otherNeighbors = (Set<V>) ((Graph<V>) other).getNeighbors(vertex);

            if (!thisNeighbors.equals(otherNeighbors)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getVertices().hashCode();
        for (V v1 : getVertices()) {
            result = 31 * result + getNeighbors(v1).hashCode();
        }
        return result;
    }

    @Override
    public void setTopologicalSortStrategy(TopologicalSortStrategy<V> strategy) {
        this.topologicalSortStrategy = strategy;
    }

    @Override
    public TopologicalSortStrategy<V> getTopologicalSortStrategy() {
        return this.topologicalSortStrategy;
    }
}