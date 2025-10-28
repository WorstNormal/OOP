package ru.nsu.gaev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

/**
 * Абстрактный базовый класс для всех реализаций графа.
 * Содержит общую логику чтения из файла и реализации equals/hashCode.
 *
 * @param <V> тип вершины
 */
public abstract class AbstractGraph<V> implements Graph<V> {

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
            // Очищаем конкретную реализацию графа
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
                // Вызываем абстрактный addVertex, который реализуют наследники
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

        // Используем raw-тип для безопасного взаимодействия с возможными различными
        // параметризациями Graph<?> у сравниваемого объекта. Это устраняет проблему
        // "incompatible types: V cannot be converted to capture of ?" при вызове
        // getNeighbors у объекта с другой параметризацией.
        Graph<?> other = (Graph<?>) o;

        // Сравнение множеств вершин (equals корректно сравнивает по элементам).
        if (!this.getVertices().equals(other.getVertices())) {
            return false;
        }

        for (V vertex : this.getVertices()) {
            Set<V> thisNeighbors = this.getNeighbors(vertex);

            // Проверяем, что у другого графа есть такая вершина (множество вершин уже равно,
            // но на всякий случай оставим проверку).
            if (!other.getVertices().contains(vertex)) {
                return false;
            }

            // Получаем соседей другого графа через raw-тип, затем безопасно приводим к Set<V>.
            @SuppressWarnings("unchecked")
            Set<V> otherNeighbors = (Set<V>) ((Graph) other).getNeighbors(vertex);

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
}