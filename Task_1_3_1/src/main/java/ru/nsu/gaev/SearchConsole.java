package ru.nsu.gaev;

import java.util.List;

/**
 * Класс, отвечающий за вывод сообщений в консоль для задачи поиска подстроки.
 */
public class SearchConsole {

    public void welcomeSearch() {
        System.out.println("Welcome to Substring Search App (Task 1.3.1)!");
    }

    public void askForFileGeneration() {
        System.out.println("Type 'generate' to create a large test file, or 'input' to use existing file:");
    }

    public void generationStarted(String fileName) {
        System.out.println("Generating large file '" + fileName + "' (~0.2GB)... Please wait.");
    }

    public void generationFinished() {
        System.out.println("File generation complete.");
    }

    public void askFileName() {
        System.out.print("Enter file name (default: input.txt): ");
    }

    public void askPattern() {
        System.out.print("Enter substring to find: ");
    }

    public void searchStarted() {
        System.out.println("Search started...");
    }

    public void searchFinished(long duration) {
        System.out.println("Search finished in " + duration + " ms.");
    }

    public void printResults(List<Long> results) {
        System.out.println("Total occurrences found: " + results.size());
        if (results.isEmpty()) {
            System.out.println("Pattern not found.");
        } else if (results.size() <= 20) {
            System.out.println("Indices: " + results);
        } else {
            System.out.println("First 10 indices: " + results.subList(0, 10) + " ...");
        }
    }

    public void errorMessage(String error) {
        // Используем System.out вместо System.err, чтобы сообщения не перемешивались
        System.out.println("Error occurred: " + error);
    }

    public void searchAgain() {
        // println гарантирует, что текст появится сразу
        System.out.println("\nPerform another search? (yes/no): ");
    }

    public void yesOrNo() {
        System.out.print("Please enter 'yes' or 'no': ");
    }

    public void goodbye() {
        System.out.println("Exiting application.");
    }
}