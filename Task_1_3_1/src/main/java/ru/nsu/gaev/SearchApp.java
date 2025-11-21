package ru.nsu.gaev;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, реализующий основную логику приложения поиска подстроки.
 * Связывает пользовательский ввод, файловую систему и алгоритм поиска.
 */
public class SearchApp {
    public static final Scanner scanner = new Scanner(System.in);
    public static SearchConsole message = new SearchConsole();

    /**
     * Точка входа в приложение.
     */
    public static void main(String[] args) {
        message.welcomeSearch();

        while (true) {
            try {
                // Шаг 1: Подготовка файла (выбор существующего или генерация теста)
                message.askForFileGeneration();
                String choice = scanner.nextLine().toLowerCase();
                String fileName = "input.txt"; // Имя по умолчанию

                if (choice.equals("generate")) {
                    fileName = "large_test_input.txt";
                    message.generationStarted(fileName);
                    FileProvider.generateBigFile(fileName, "бра"); // Генерируем файл с паттерном "бра"
                    message.generationFinished();
                } else {
                    message.askFileName();
                    String inputName = scanner.nextLine();
                    if (!inputName.isBlank()) {
                        fileName = inputName;
                    }
                }

                // Шаг 2: Ввод подстроки
                message.askPattern();
                String pattern = scanner.nextLine();

                // Шаг 3: Запуск поиска
                message.searchStarted();
                long startTime = System.currentTimeMillis();

                KmpLogic logic = new KmpLogic();
                // Передаем Reader, который создает FileProvider
                List<Long> results = logic.findPattern(FileProvider.getReader(fileName), pattern);

                long endTime = System.currentTimeMillis();

                // Шаг 4: Вывод результатов
                message.searchFinished(endTime - startTime);
                message.printResults(results);

            } catch (IOException e) {
                message.errorMessage(e.getMessage());
            }

            // Вопрос о повторении (как в Blackjack)
            message.searchAgain();
            String answer = scanner.nextLine().toLowerCase();
            while (!answer.equals("yes") && !answer.equals("no")) {
                message.yesOrNo();
                answer = scanner.nextLine().toLowerCase();
            }
            if (answer.equals("no")) {
                message.goodbye();
                break;
            }
        }
    }
}