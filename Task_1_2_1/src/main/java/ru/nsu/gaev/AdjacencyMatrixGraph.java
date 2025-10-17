package ru.nsu.gaev;

import java.util.*;

public class AdjacencyMatrixGraph<V> extends AbstractGraph<V> {
    private int[][] matrix = new int[0][0];

    @Override
    public void clear() {
        matrix = new int[0][0];
    }

    @Override
    public boolean addVertex(V vertex) {
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
        throw new UnsupportedOperationException("Remove vertex is not supported.");
    }

    @Override
    public boolean addEdge(V source, V destination) {
        Integer srcIdx = vertexToIndex.get(source);
        Integer destIdx = vertexToIndex.get(destination);
        if (srcIdx == null || destIdx == null) throw new IllegalArgumentException("Vertex not found.");
        if (matrix[srcIdx][destIdx] == 1) return false;
        matrix[srcIdx][destIdx] = 1;
        return true;
    }

    @Override
    public boolean removeEdge(V source, V destination) {
        Integer srcIdx = vertexToIndex.get(source);
        Integer destIdx = vertexToIndex.get(destination);
        if (srcIdx == null || destIdx == null || matrix[srcIdx][destIdx] == 0) return false;
        matrix[srcIdx][destIdx] = 0;
        return true;
    }

    @Override
    public Set<V> getNeighbors(V vertex) {
        Integer index = vertexToIndex.get(vertex);
        if (index == null) return Collections.emptySet();
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
        for (V v : indexToVertex) sb.append(v).append(" ");
        sb.append("\n");
        for (int i = 0; i < matrix.length; i++) {
            sb.append(String.format("%-4s", indexToVertex.get(i)));
            for (int j = 0; j < matrix[i].length; j++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}