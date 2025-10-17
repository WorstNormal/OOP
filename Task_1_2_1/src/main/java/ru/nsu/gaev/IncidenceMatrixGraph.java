package ru.nsu.gaev;

import java.util.*;

public class IncidenceMatrixGraph<V> extends AbstractGraph<V> {
    private int[][] matrix = new int[0][0];
    private int edgeCount = 0;

    @Override
    public void clear() {
        matrix = new int[0][0];
        edgeCount = 0;
    }

    @Override
    public boolean addVertex(V vertex) {
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
        throw new UnsupportedOperationException("Remove vertex is not supported.");
    }

    @Override
    public boolean addEdge(V source, V destination) {
        Integer srcIdx = vertexToIndex.get(source);
        Integer destIdx = vertexToIndex.get(destination);
        if (srcIdx == null || destIdx == null) throw new IllegalArgumentException("Vertex not found.");

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
        throw new UnsupportedOperationException("Remove edge is not supported.");
    }

    @Override
    public Set<V> getNeighbors(V vertex) {
        Integer index = vertexToIndex.get(vertex);
        if (index == null) return Collections.emptySet();

        Set<V> neighbors = new LinkedHashSet<>();
        for (int j = 0; j < edgeCount; j++) {
            if (matrix[index][j] == 1) {
                for (int i = 0; i < indexToVertex.size(); i++) {
                    if (matrix[i][j] == -1) {
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
        if (srcIdx == null || destIdx == null) return false;

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
        for (int j = 0; j < edgeCount; j++) sb.append(String.format("%3d", j));
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