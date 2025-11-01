package ru.nsu.gaev;

import java.io.IOException;
import java.util.List;

/**
 * Утилитный класс для запуска демонстрации и тестирования реализаций Graph.
 */
public class Main {
    /**
     * Точка входа. Считывает граф из файла graph.txt и демонстрирует
     * работу реализаций и топологическую сортировку.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        try {
            Graph<String> listGraph = new AdjacencyListGraph<>();
            Graph<String> matrixGraph = new AdjacencyMatrixGraph<>();
            Graph<String> incidenceGraph = new IncidenceMatrixGraph<>();

            listGraph.readFromFile("graph.txt");
            matrixGraph.readFromFile("graph.txt");
            incidenceGraph.readFromFile("graph.txt");

            System.out.println(listGraph);
            System.out.println(matrixGraph);
            System.out.println(incidenceGraph);

            System.out.println("List vs Matrix equals: " + listGraph.equals(matrixGraph));
            System.out.println("List vs Incidence equals: " + listGraph.equals(incidenceGraph));
            System.out.println("----------------------------------------");

            System.out.println("Topological Sort (from Adjacency List implementation):");
            List<String> sortedVertices = listGraph.topologicalSort();
            System.out.println(sortedVertices);

        } catch (IOException e) {
            System.err.println("Error reading or parsing graph file: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Error during sorting: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected runtime error occurred: " + e.getMessage());
        }
    }
}
