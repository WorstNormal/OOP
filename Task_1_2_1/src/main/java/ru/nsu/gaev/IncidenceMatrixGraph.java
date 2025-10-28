package ru.nsu.gaev;

import java.util.*;

// Наследуемся от AbstractIndexBasedGraph
public class IncidenceMatrixGraph<V> extends AbstractIndexBasedGraph<V> {
    private int[][] matrix = new int[0][0];
    private int edgeCount = 0;

    @Override
    public void clear() {
        super.clear(); // Очищаем карты (vertexToIndex, indexToVertex)
        matrix = new int[0][0]; // Очищаем свои структуры
        edgeCount = 0;
    }

    @Override
    public boolean addVertex(V vertex) {
        // Вызываем super.addVertex() из AbstractIndexBasedGraph
        if (super.addVertex(vertex)) {
            int newSize = indexToVertex.size();
            int[][] newMatrix = new int[newSize][edgeCount];
            if (matrix.length > 0) {
                for (int i = 0; i < matrix.length; i++) {
                    System.arraycopy(matrix[i], 0, newMatrix[i], 0, edgeCount);
                }
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

        int oldVertexCount = indexToVertex.size();
        int newVertexCount = oldVertexCount - 1;

        Set<Integer> edgesToRemove = new HashSet<>();
        for (int j = 0; j < edgeCount; j++) {
            if (matrix[idxToRemove][j] != 0) {
                edgesToRemove.add(j);
            }
        }
        int newEdgeCount = edgeCount - edgesToRemove.size();

        if (newVertexCount == 0) {
            clear(); // Вызовет super.clear() и очистит matrix
            return true;
        }

        int[][] newMatrix = new int[newVertexCount][newEdgeCount];

        // 2. Удаляем вершину из списков (в super-классе)
        vertexToIndex.remove(vertex);
        indexToVertex.remove((int) idxToRemove);

        // 3. Перестраиваем карту vertexToIndex (в super-классе)
        vertexToIndex.clear();
        for (int i = 0; i < newVertexCount; i++) {
            vertexToIndex.put(indexToVertex.get(i), i);
        }

        // 4. Перестраиваем матрицу
        int r = 0;
        for (int i = 0; i < oldVertexCount; i++) {
            if (i == idxToRemove) {
                continue;
            }
            int c = 0;
            for (int j = 0; j < edgeCount; j++) {
                if (edgesToRemove.contains(j)) {
                    continue;
                }
                newMatrix[r][c] = matrix[i][j];
                c++;
            }
            r++;
        }

        matrix = newMatrix;
        edgeCount = newEdgeCount;
        return true;
    }

    /**
     * Эта реализация addEdge исправляет ошибку компиляции.
     */
    @Override
    public boolean addEdge(V source, V destination) {
        Integer srcIdx = vertexToIndex.get(source);
        Integer destIdx = vertexToIndex.get(destination);
        if (srcIdx == null || destIdx == null) {
            throw new IllegalArgumentException("Vertex not found.");
        }

        // Проверка на существование ребра (дорогая операция, но нужная)
        if (hasEdge(source, destination)) {
            return false;
        }

        edgeCount++;
        int[][] newMatrix = new int[indexToVertex.size()][edgeCount];
        for (int i = 0; i < indexToVertex.size(); i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, edgeCount - 1);
        }
        matrix = newMatrix;
        matrix[srcIdx][edgeCount - 1] = 1;
        matrix[destIdx][edgeCount - 1] = -1;
        return true;
    }

    @Override
    public boolean removeEdge(V source, V destination) {
        Integer srcIdx = vertexToIndex.get(source);
        Integer destIdx = vertexToIndex.get(destination);
        if (srcIdx == null || destIdx == null) {
            return false;
        }

        // Находим столбец (edge) где src->1 и dest->-1
        int foundCol = -1;
        for (int j = 0; j < edgeCount; j++) {
            if (matrix[srcIdx][j] == 1 && matrix[destIdx][j] == -1) {
                foundCol = j;
                break;
            }
        }

        if (foundCol == -1) {
            return false; // ребро не найдено
        }

        // Если это единственный столбец, просто обнулим матрицу столбцов
        if (edgeCount == 1) {
            matrix = new int[indexToVertex.size()][0];
            edgeCount = 0;
            return true;
        }

        int newEdgeCount = edgeCount - 1;
        int[][] newMatrix = new int[indexToVertex.size()][newEdgeCount];

        for (int i = 0; i < indexToVertex.size(); i++) {
            int c = 0;
            for (int j = 0; j < edgeCount; j++) {
                if (j == foundCol) {
                    continue;
                }
                newMatrix[i][c++] = matrix[i][j];
            }
        }

        matrix = newMatrix;
        edgeCount = newEdgeCount;
        return true;
    }

    @Override
    public Set<V> getNeighbors(V vertex) {
        Integer index = vertexToIndex.get(vertex);
        if (index == null) {
            return Collections.emptySet();
        }

        Set<V> neighbors = new LinkedHashSet<>();
        for (int j = 0; j < edgeCount; j++) {
            if (matrix[index][j] == 1) { // Если это исходящее ребро
                for (int i = 0; i < indexToVertex.size(); i++) {
                    if (matrix[i][j] == -1) { // Находим, куда оно входит
                        neighbors.add(indexToVertex.get(i));
                        break;
                    }
                }
            }
        }
        return neighbors;
    }

    @Override
    public boolean hasEdge(V source, V destination) {
        Integer srcIdx = vertexToIndex.get(source);
        Integer destIdx = vertexToIndex.get(destination);
        if (srcIdx == null || destIdx == null) {
            return false;
        }

        for (int j = 0; j < edgeCount; j++) {
            if (matrix[srcIdx][j] == 1 && matrix[destIdx][j] == -1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Incidence Matrix Graph (Edges 0 to " + (edgeCount - 1) + "):\n");
        sb.append("    ");
        for (int j = 0; j < edgeCount; j++) {
            sb.append(String.format("%3d", j));
        }
        sb.append("\n");
        for (int i = 0; i < indexToVertex.size(); i++) {
            sb.append(String.format("%-4s", indexToVertex.get(i)));
            for (int j = 0; j < edgeCount; j++) {
                sb.append(String.format("%3d", matrix[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}