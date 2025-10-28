package ru.nsu.gaev;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для IncidenceMatrixGraph
 */
class IncidenceMatrixGraphTest {

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
    @DisplayName("Добавление вершин и проверка getVertices")
    void testAddVertex() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
        assertTrue(graph.addVertex("A"));
        assertTrue(graph.getVertices().contains("A"));
        assertEquals(1, graph.getVertices().size());

        assertTrue(graph.addVertex("B"));
        assertTrue(graph.getVertices().containsAll(Set.of("A", "B")));
        assertEquals(2, graph.getVertices().size());
    }

    @Test
    @DisplayName("Запрет на добавление дубликатов вершин")
    void testAddDuplicateVertex() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
        graph.addVertex("A");
        assertFalse(graph.addVertex("A"));
        assertEquals(1, graph.getVertices().size());
    }

    @Test
    @DisplayName("Добавление ребер и проверка hasEdge / getNeighbors")
    void testAddEdge() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
        setupGraph(graph); // A->B, A->C, C->D

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
    @DisplayName("Добавление дубликатов ребер (возвращает false)")
    void testAddDuplicateEdge() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        assertTrue(graph.addEdge("A", "B"));

        // IncidenceMatrixGraph.addEdge() возвращает false для дубликатов
        assertFalse(graph.addEdge("A", "B"));

        assertTrue(graph.hasEdge("A", "B"));
        // Убедимся, что ребро не добавилось дважды
        assertEquals(1, graph.getNeighbors("A").size());
    }


    @Test
    @DisplayName("Исключение при добавлении ребра к несуществующей вершине")
    void testAddEdge_VertexNotFound() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
        graph.addVertex("A");
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("A", "Z"));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("Z", "A"));
    }

    @Test
    @DisplayName("Удаление вершины (с переиндексацией)")
    void testRemoveVertex() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
        setupGraph(graph); // A->B, A->C, C->D
        graph.addVertex("E"); // Изолированная
        graph.addEdge("B", "C"); // B -> C

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
    @DisplayName("Удаление всех вершин")
    void testRemoveAllVertices() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
        setupGraph(graph);

        assertTrue(graph.removeVertex("A"));
        assertTrue(graph.removeVertex("B"));
        assertTrue(graph.removeVertex("C"));
        assertTrue(graph.removeVertex("D"));

        assertEquals(0, graph.getVertices().size());
        assertTrue(graph.getVertices().isEmpty());
    }

    @Test
    @DisplayName("Удаление ребра (поддерживается)")
    void testRemoveEdge() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
        setupGraph(graph); // A->B, A->C, C->D

        // Удаляем существующее ребро A -> B
        assertTrue(graph.removeEdge("A", "B"));
        assertFalse(graph.hasEdge("A", "B"));
        // После удаления у A должен остаться только C
        assertEquals(Set.of("C"), graph.getNeighbors("A"));

        // Попытка удалить несуществующее ребро должна вернуть false
        assertFalse(graph.removeEdge("A", "B"));
        assertFalse(graph.removeEdge("A", "Z"));
    }

    @Test
    @DisplayName("Метод clear()")
    void testClear() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
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
    @DisplayName("Чтение из файла (readFromFile)")
    void testReadFromFile(@TempDir Path tempDir) throws IOException {
        Graph<String> graph = new IncidenceMatrixGraph<>();
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
    @DisplayName("Топологическая сортировка (успех)")
    void testTopologicalSort() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
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
    @DisplayName("Топологическая сортировка (обнаружение цикла)")
    void testTopologicalSort_Cycle() {
        Graph<String> graph = new IncidenceMatrixGraph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A"); // Цикл

        assertThrows(IllegalStateException.class, graph::topologicalSort);
    }

    @Test
    @DisplayName("Проверка equals() и hashCode()")
    void testEqualsAndHashCode() {
        Graph<String> graph1 = new IncidenceMatrixGraph<>();
        setupGraph(graph1);

        Graph<String> graph2 = new IncidenceMatrixGraph<>();
        setupGraph(graph2);

        assertEquals(graph1, graph2);
        assertEquals(graph1.hashCode(), graph2.hashCode());

        graph2.addVertex("E");
        assertNotEquals(graph1, graph2);
    }
}