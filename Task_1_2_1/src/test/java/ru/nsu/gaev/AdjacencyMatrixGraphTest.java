package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для AdjacencyMatrixGraph.
 */
class AdjacencyMatrixGraphTest {

    /**
     * Вспомогательный метод для создания стандартного графа для тестов.
     * A -> B
     * |
     * v
     * C -> D
     */
    private void setupGraph(Graph<String> graph) {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("C", "D");
    }

    @Test
    void testAddVertex() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        assertTrue(graph.addVertex("A"));
        assertTrue(graph.getVertices().contains("A"));
        assertEquals(1, graph.getVertices().size());

        assertTrue(graph.addVertex("B"));
        assertTrue(graph.getVertices().containsAll(Set.of("A", "B")));
        assertEquals(2, graph.getVertices().size());
    }

    @Test
    void testAddDuplicateVertex() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("A");
        assertFalse(graph.addVertex("A"));
        assertEquals(1, graph.getVertices().size());
    }

    @Test
    void testAddEdge() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        setupGraph(graph);

        assertTrue(graph.hasEdge("A", "B"));
        assertTrue(graph.hasEdge("A", "C"));
        assertTrue(graph.hasEdge("C", "D"));

        assertFalse(graph.hasEdge("B", "A"));
        assertFalse(graph.hasEdge("A", "D"));

        assertEquals(Set.of("B", "C"), graph.getNeighbors("A"));
        assertEquals(Set.of("D"), graph.getNeighbors("C"));
        assertEquals(Set.of(), graph.getNeighbors("B"));
        assertEquals(Set.of(), graph.getNeighbors("D"));
    }

    @Test
    void testAddDuplicateEdge() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        assertTrue(graph.addEdge("A", "B"));
        assertFalse(graph.addEdge("A", "B"));

        assertTrue(graph.hasEdge("A", "B"));
        assertEquals(1, graph.getNeighbors("A").size());
    }


    @Test
    void testAddEdge_VertexNotFound() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("A");
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("A", "Z"));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("Z", "A"));
    }

    @Test
    void testRemoveVertex() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        setupGraph(graph);
        graph.addVertex("E");
        graph.addEdge("B", "C");

        assertTrue(graph.removeVertex("C"));

        assertEquals(4, graph.getVertices().size());
        assertFalse(graph.getVertices().contains("C"));

        assertFalse(graph.hasEdge("A", "C"));
        assertFalse(graph.hasEdge("B", "C"));
        assertFalse(graph.hasEdge("C", "D"));

        assertTrue(graph.hasEdge("A", "B"));

        assertEquals(Set.of("B"), graph.getNeighbors("A"));
        assertEquals(Set.of(), graph.getNeighbors("B"));

        assertFalse(graph.removeVertex("Z"));
        assertFalse(graph.removeVertex("C"));
    }

    @Test
    void testRemoveAllVertices() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        setupGraph(graph);

        assertTrue(graph.removeVertex("A"));
        assertTrue(graph.removeVertex("B"));
        assertTrue(graph.removeVertex("C"));
        assertTrue(graph.removeVertex("D"));

        assertEquals(0, graph.getVertices().size());
        assertTrue(graph.getVertices().isEmpty());
    }

    @Test
    void testRemoveEdge() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        setupGraph(graph);

        assertTrue(graph.removeEdge("A", "C"));
        assertFalse(graph.hasEdge("A", "C"));
        assertEquals(Set.of("B"), graph.getNeighbors("A"));

        assertFalse(graph.removeEdge("A", "D"));
        assertFalse(graph.removeEdge("A", "C"));
    }

    @Test
    void testClear() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        setupGraph(graph);
        assertFalse(graph.getVertices().isEmpty());

        graph.clear();
        assertTrue(graph.getVertices().isEmpty());
        assertEquals(0, graph.getVertices().size());

        graph.addVertex("New");
        graph.addVertex("Node");
        graph.addEdge("New", "Node");
        assertTrue(graph.hasEdge("New", "Node"));
        assertEquals(2, graph.getVertices().size());
    }

    @Test
    void testReadFromFile(@TempDir Path tempDir) throws IOException {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("TRASH");
        graph.addEdge("TRASH", "TRASH");

        String fileContent = """
                5
                V1
                V2
                V3
                V4
                V5
                4
                V1 V2
                V1 V3
                V3 V4
                V3 V5
                """;

        Path file = tempDir.resolve("graph.txt");
        Files.writeString(file, fileContent);

        graph.readFromFile(file.toString());

        assertEquals(5, graph.getVertices().size());
        assertFalse(graph.getVertices().contains("TRASH"));
        assertTrue(graph.hasEdge("V1", "V2"));
        assertTrue(graph.hasEdge("V1", "V3"));
        assertFalse(graph.hasEdge("V2", "V1"));
    }

    @Test
    void testTopologicalSort() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addEdge("1", "2");
        graph.addEdge("1", "3");
        graph.addEdge("2", "4");
        graph.addEdge("3", "4");

        List<String> sorted = graph.topologicalSort();
        assertEquals(5, sorted.size());
        assertTrue(sorted.indexOf("1") < sorted.indexOf("2"));
        assertTrue(sorted.indexOf("1") < sorted.indexOf("3"));
        assertTrue(sorted.indexOf("2") < sorted.indexOf("4"));
        assertTrue(sorted.indexOf("3") < sorted.indexOf("4"));
    }

    @Test
    void testTopologicalSort_Cycle() {
        Graph<String> graph = new AdjacencyMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");

        assertThrows(IllegalStateException.class, graph::topologicalSort);
    }

    @Test
    void testEqualsAndHashCode() {
        Graph<String> graph1 = new AdjacencyMatrixGraph<>();
        setupGraph(graph1);

        Graph<String> graph2 = new AdjacencyMatrixGraph<>();
        setupGraph(graph2);

        assertEquals(graph1, graph2);
        assertEquals(graph1.hashCode(), graph2.hashCode());

        graph2.addVertex("E");
        assertNotEquals(graph1, graph2);
    }
}