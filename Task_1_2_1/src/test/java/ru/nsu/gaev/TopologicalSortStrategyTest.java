package ru.nsu.gaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.algorithms.DFSTopologicalSortStrategy;
import ru.nsu.gaev.algorithms.KahnTopologicalSortStrategy;
import ru.nsu.gaev.algorithms.TopologicalSortStrategy;
import ru.nsu.gaev.graph.Graph;
import ru.nsu.gaev.impl.AdjacencyListGraph;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для стратегий топологической сортировки.
 */
class TopologicalSortStrategyTest {

    private Graph<String> graph;

    /**
     * Создает тестовый граф:
     * 1 -> 2 -> 4
     * 1 -> 3 -> 4
     * 5 (изолированная вершина).
     */
    @BeforeEach
    void setUp() {
        graph = new AdjacencyListGraph<>();
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addEdge("1", "2");
        graph.addEdge("1", "3");
        graph.addEdge("2", "4");
        graph.addEdge("3", "4");
    }

    @Test
    void testKahnTopologicalSort() {
        TopologicalSortStrategy<String> strategy = new KahnTopologicalSortStrategy<>();
        List<String> sorted = strategy.sort(graph);

        assertEquals(5, sorted.size());
        assertTrue(sorted.containsAll(graph.getVertices()));

        // Проверяем топологический порядок
        assertTrue(sorted.indexOf("1") < sorted.indexOf("2"));
        assertTrue(sorted.indexOf("1") < sorted.indexOf("3"));
        assertTrue(sorted.indexOf("2") < sorted.indexOf("4"));
        assertTrue(sorted.indexOf("3") < sorted.indexOf("4"));
    }

    @Test
    void testDfsTopologicalSort() {
        TopologicalSortStrategy<String> strategy = new DFSTopologicalSortStrategy<>();
        List<String> sorted = strategy.sort(graph);

        assertEquals(5, sorted.size());
        assertTrue(sorted.containsAll(graph.getVertices()));

        // Проверяем топологический порядок
        assertTrue(sorted.indexOf("1") < sorted.indexOf("2"));
        assertTrue(sorted.indexOf("1") < sorted.indexOf("3"));
        assertTrue(sorted.indexOf("2") < sorted.indexOf("4"));
        assertTrue(sorted.indexOf("3") < sorted.indexOf("4"));
    }

    @Test
    void testKahnAndDfsProduceValidSort() {
        TopologicalSortStrategy<String> kahnStrategy = new KahnTopologicalSortStrategy<>();
        TopologicalSortStrategy<String> dfsStrategy = new DFSTopologicalSortStrategy<>();

        List<String> kahnResult = kahnStrategy.sort(graph);
        List<String> dfsResult = dfsStrategy.sort(graph);

        // Оба алгоритма должны производить валидную топологическую сортировку
        assertEquals(5, kahnResult.size());
        assertEquals(5, dfsResult.size());

        // Проверяем, что оба результата содержат все вершины
        assertTrue(kahnResult.containsAll(graph.getVertices()));
        assertTrue(dfsResult.containsAll(graph.getVertices()));

        // Проверяем топологический порядок в обоих результатах
        validateTopologicalOrder(kahnResult);
        validateTopologicalOrder(dfsResult);
    }

    @Test
    void testKahnTopologicalSort_Cycle() {
        Graph<String> cyclicGraph = new AdjacencyListGraph<>();
        cyclicGraph.addVertex("A");
        cyclicGraph.addVertex("B");
        cyclicGraph.addVertex("C");
        cyclicGraph.addEdge("A", "B");
        cyclicGraph.addEdge("B", "C");
        cyclicGraph.addEdge("C", "A");

        TopologicalSortStrategy<String> strategy = new KahnTopologicalSortStrategy<>();
        assertThrows(IllegalStateException.class, () -> strategy.sort(cyclicGraph));
    }

    @Test
    void testDfsTopologicalSortCycle() {
        Graph<String> cyclicGraph = new AdjacencyListGraph<>();
        cyclicGraph.addVertex("A");
        cyclicGraph.addVertex("B");
        cyclicGraph.addVertex("C");
        cyclicGraph.addEdge("A", "B");
        cyclicGraph.addEdge("B", "C");
        cyclicGraph.addEdge("C", "A");

        TopologicalSortStrategy<String> strategy = new DFSTopologicalSortStrategy<>();
        assertThrows(IllegalStateException.class, () -> strategy.sort(cyclicGraph));
    }

    @Test
    void testKahnTopologicalSort_SingleVertex() {
        Graph<String> singleVertexGraph = new AdjacencyListGraph<>();
        singleVertexGraph.addVertex("A");

        TopologicalSortStrategy<String> strategy = new KahnTopologicalSortStrategy<>();
        List<String> sorted = strategy.sort(singleVertexGraph);

        assertEquals(1, sorted.size());
        assertEquals("A", sorted.get(0));
    }

    @Test
    void testDfsTopologicalSortSingleVertex() {
        Graph<String> singleVertexGraph = new AdjacencyListGraph<>();
        singleVertexGraph.addVertex("A");

        TopologicalSortStrategy<String> strategy = new DFSTopologicalSortStrategy<>();
        List<String> sorted = strategy.sort(singleVertexGraph);

        assertEquals(1, sorted.size());
        assertEquals("A", sorted.get(0));
    }

    @Test
    void testKahnTopologicalSort_EmptyGraph() {
        Graph<String> emptyGraph = new AdjacencyListGraph<>();

        TopologicalSortStrategy<String> strategy = new KahnTopologicalSortStrategy<>();
        List<String> sorted = strategy.sort(emptyGraph);

        assertEquals(0, sorted.size());
        assertTrue(sorted.isEmpty());
    }

    @Test
    void testDfsTopologicalSortEmptyGraph() {
        Graph<String> emptyGraph = new AdjacencyListGraph<>();

        TopologicalSortStrategy<String> strategy = new DFSTopologicalSortStrategy<>();
        List<String> sorted = strategy.sort(emptyGraph);

        assertEquals(0, sorted.size());
        assertTrue(sorted.isEmpty());
    }

    @Test
    void testKahnTopologicalSort_LinearGraph() {
        Graph<String> linearGraph = new AdjacencyListGraph<>();
        linearGraph.addVertex("1");
        linearGraph.addVertex("2");
        linearGraph.addVertex("3");
        linearGraph.addEdge("1", "2");
        linearGraph.addEdge("2", "3");

        TopologicalSortStrategy<String> strategy = new KahnTopologicalSortStrategy<>();
        List<String> sorted = strategy.sort(linearGraph);

        assertEquals(3, sorted.size());
        assertEquals("1", sorted.get(0));
        assertEquals("2", sorted.get(1));
        assertEquals("3", sorted.get(2));
    }

    @Test
    void testDfsTopologicalSortLinearGraph() {
        Graph<String> linearGraph = new AdjacencyListGraph<>();
        linearGraph.addVertex("1");
        linearGraph.addVertex("2");
        linearGraph.addVertex("3");
        linearGraph.addEdge("1", "2");
        linearGraph.addEdge("2", "3");

        TopologicalSortStrategy<String> strategy = new DFSTopologicalSortStrategy<>();
        List<String> sorted = strategy.sort(linearGraph);

        assertEquals(3, sorted.size());
        // DFS может дать другой порядок, но топологический порядок должен соблюдаться
        assertTrue(sorted.indexOf("1") < sorted.indexOf("2"));
        assertTrue(sorted.indexOf("2") < sorted.indexOf("3"));
    }

    /**
     * Вспомогательный метод для проверки топологического порядка.
     */
    private void validateTopologicalOrder(List<String> sorted) {
        // Проверяем, что для каждого ребра (u, v) в графе u идет перед v
        for (String vertex : graph.getVertices()) {
            int vertexIndex = sorted.indexOf(vertex);
            for (String neighbor : graph.getNeighbors(vertex)) {
                int neighborIndex = sorted.indexOf(neighbor);
                assertTrue(vertexIndex < neighborIndex,
                        "Vertex " + vertex + " should come before " + neighbor);
            }
        }
    }
}

