package ru.nsu.gaev;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Реализация графа через матрицу смежности.
 * Наследуется от AbstractIndexBasedGraph.
 *
 * @param <V> тип вершины
 */
public class AdjacencyMatrixGraph<V> extends AbstractIndexBasedGraph<V> {
    private int[][] matrix = new int[0][0];

    @Override
    public void clear() {
        super.clear(); // Очищаем карты (vertexToIndex, indexToVertex)
        matrix = new int[0][0]; // Очищаем свою структуру
    }

    @Override
    public boolean addVertex(V vertex) {
        // Вызываем super.addVertex() из AbstractIndexBasedGraph
        if (super.addVertex(vertex)) {
            int newSize = indexToVertex.size();
            int[][] newMatrix = new int[newSize][newSize];
            for (int i = 0; i < matrix.length; i++) {
                System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix.length);
            }
            matrix = newMatrix;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeVertex(V vertex) {
        Integer idxToRemove = vertexToIndex.get(vertex);
        if (idxToRemove == null) {
            return false;
        }

        int oldSize = indexToVertex.size();
        int newSize = oldSize - 1;

        if (newSize == 0) {
            clear(); // Вызовет super.clear() и очистит matrix
            return true;
        }

        int[][] newMatrix = new int[newSize][newSize];

        // 1. Удаляем вершину из списков (они в super-классе)
        vertexToIndex.remove(vertex);
        indexToVertex.remove((int) idxToRemove);

        // 2. Перестраиваем карту vertexToIndex
        vertexToIndex.clear();
        for (int i = 0; i < newSize; i++) {
            vertexToIndex.put(indexToVertex.get(i), i);
        }

        // 3. Перестраиваем матрицу
        int r = 0;
        for (int i = 0; i < oldSize; i++) {
            if (i == idxToRemove) {
                continue;
            }
            int c = 0;
            for (int j = 0; j < oldSize; j++) {
                if (j == idxToRemove) {
                    continue;
                }
                newMatrix[r][c] = matrix[i][j];
                c++;
            }
            r++;
        }

        matrix = newMatrix;
        return true;
    }

    @Override
    public boolean addEdge(V source, V destination) {
        Integer srcIdx = vertexToIndex.get(source);
        Integer destIdx = vertexToIndex.get(destination);
        if (srcIdx == null || destIdx == null) {
            throw new IllegalArgumentException("Vertex not found.");
        }
        if (matrix[srcIdx][destIdx] == 1) {
            return false;
        }
        matrix[srcIdx][destIdx] = 1;
        return true;
    }

    @Override
    public boolean removeEdge(V source, V destination) {
        Integer srcIdx = vertexToIndex.get(source);
        Integer destIdx = vertexToIndex.get(destination);
        if (srcIdx == null || destIdx == null || matrix[srcIdx][destIdx] == 0) {
            return false;
        }
        matrix[srcIdx][destIdx] = 0;
        return true;
    }

    @Override
    public Set<V> getNeighbors(V vertex) {
        Integer index = vertexToIndex.get(vertex);
        if (index == null) {
            return Collections.emptySet();
        }
        Set<V> neighbors = new LinkedHashSet<>();
        for (int j = 0; j < matrix[index].length; j++) {
            if (matrix[index][j] == 1) {
                neighbors.add(indexToVertex.get(j));
            }
        }
        return neighbors;
    }

    @Override
    public boolean hasEdge(V source, V destination) {
        Integer srcIdx = vertexToIndex.get(source);
        Integer destIdx = vertexToIndex.get(destination);
        return srcIdx != null && destIdx != null && matrix[srcIdx][destIdx] == 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Adjacency Matrix Graph:\n");
        sb.append("    ");
        for (V v : indexToVertex) {
            sb.append(String.format("%-3s", v));
        }
        sb.append("\n");
        for (int i = 0; i < matrix.length; i++) {
            sb.append(String.format("%-4s", indexToVertex.get(i)));
            for (int j = 0; j < matrix[i].length; j++) {
                sb.append(String.format("%-3d", matrix[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}