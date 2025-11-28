package ru.nsu.gaev;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, реализующий основную логику приложения поиска подстроки.
 * Связывает пользовательский ввод, файловую систему и алгоритм поиска.
 */
public class SearchApp {

    public static SearchConsole message = new SearchConsole();

    /**
     * Точка входа в приложение.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        message.welcomeSearch();

        while (true) {
            try {
                // Шаг 1: Подготовка файла
                message.askForFileGeneration();

                // Если ввода нет (Ctrl+D), выходим
                if (!scanner.hasNextLine()) {
                    break;
                }

                String choice = scanner.nextLine().trim().toLowerCase();
                String fileName = "input.txt";

                if (choice.equals("generate")) {
                    fileName = "large_test_input.txt";
                    message.generationStarted(fileName);
                    FileProvider.generateBigFile(fileName, "бра");
                    message.generationFinished();
                } else {
                    message.askFileName();
                    if (scanner.hasNextLine()) {
                        String inputName = scanner.nextLine().trim();
                        if (!inputName.isBlank()) {
                            fileName = inputName;
                        }
                    }
                }

                // Шаг 2: Ввод подстроки
                message.askPattern();
                String pattern = "";
                if (scanner.hasNextLine()) {
                    pattern = scanner.nextLine();
                }

                // Шаг 3: Запуск поиска
                message.searchStarted();
                long startTime = System.currentTimeMillis();

                KmpLogic logic = new KmpLogic();
                List<Long> results;

                try (BufferedReader reader = FileProvider.getReader(fileName)) {
                    results = logic.findPattern(reader, pattern);
                }

                long endTime = System.currentTimeMillis();

                // Шаг 4: Вывод результатов
                message.searchFinished(endTime - startTime);
                message.printResults(results);

            } catch (IOException e) {
                message.errorMessage(e.getMessage());
            }

            message.searchAgain();

            // Если пользователь закрыл поток ввода (Ctrl+D), прерываем цикл
            if (!scanner.hasNextLine()) {
                break;
            }

            String answer = scanner.nextLine().trim().toLowerCase();

            while (!answer.equals("yes") && !answer.equals("no")) {
                message.yesOrNo();
                if (!scanner.hasNextLine()) {
                    answer = "no"; // Принудительный выход если поток закрылся
                    break;
                }
                answer = scanner.nextLine().trim().toLowerCase();
            }
            if (answer.equals("no")) {
                message.goodbye();
                break;
            }
        }
    }
}