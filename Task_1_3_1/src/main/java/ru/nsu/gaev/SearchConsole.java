package ru.nsu.gaev;

import java.util.List;

/**
 * Класс, отвечающий за вывод сообщений в консоль для задачи поиска подстроки.
 */
public class SearchConsole {

    /** Выводит приветственное сообщение при старте приложения. */
    public void welcomeSearch() {
        System.out.println("Welcome to Substring Search App (Task 1.3.1)!");
    }

    /** Печатает подсказку о генерации файла или использовании существующего. */
    public void askForFileGeneration() {
        System.out.println("Type 'generate' to create a large test file, "
                + "or 'input' to use existing file:");
    }

    /** Сообщение о начале генерации файла. */
    public void generationStarted(String fileName) {
        System.out.println("Generating large file '" + fileName + "'... Please wait.");
    }

    /** Сообщение о завершении генерации файла. */
    public void generationFinished() {
        System.out.println("File generation complete.");
    }

    /** Просит ввести имя файла. */
    public void askFileName() {
        System.out.print("Enter file name (default: input.txt): ");
    }

    /** Просит ввести искомую подстроку. */
    public void askPattern() {
        System.out.print("Enter substring to find: ");
    }

    /** Сообщает о начале поиска. */
    public void searchStarted() {
        System.out.println("Search started...");
    }

    /** Сообщает о завершении поиска и времени выполнения. */
    public void searchFinished(long duration) {
        System.out.println("Search finished in " + duration + " ms.");
    }

    /**
     * Печатает результаты поиска: либо количество, либо первые индексы.
     *
     * @param results список найденных позиций в файле
     */
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

    /** Выводит сообщение об ошибке. */
    public void errorMessage(String error) {
        System.out.println("Error occurred: " + error);
    }

    /** Печатает приглашение к повторному поиску. */
    public void searchAgain() {
        // println гарантирует, что текст появится сразу
        System.out.println("\nPerform another search? (yes/no): ");
    }

    /** Попросить ввести yes/no при неверном вводе. */
    public void yesOrNo() {
        System.out.print("Please enter 'yes' or 'no': ");
    }

    /** Сообщение о завершении работы приложения. */
    public void goodbye() {
        System.out.println("Exiting application.");
    }
}