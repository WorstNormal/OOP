package ru.nsu.gaev;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс {@code Parser} выполняет синтаксический разбор арифметических выражений
 * и преобразует их в объекты типа {@link Expression}.
 * Также поддерживается разбор строк с присваиванием переменных.
 *
 * <p>Поддерживаемые операторы:
 * <ul>
 *   <li>Сложение: {@code +}</li>
 *   <li>Вычитание: {@code -}</li>
 *   <li>Умножение: {@code *}</li>
 *   <li>Деление: {@code /}</li>
 * </ul>
 *
 * <p>Пример выражения:
 * <pre>(x + (3 * y))</pre>
 */
public class Parser {

    /**
     * Разбирает строку и возвращает соответствующее выражение.
     *
     * @param inputString строка с арифметическим выражением
     * @return объект {@link Expression}, соответствующий выражению
     * @throws IllegalArgumentException если входная строка некорректна
     */
    public static Expression parse(String inputString) {
        if (inputString == null) {
            throw new IllegalArgumentException("Входная строка не может быть null");
        }
        Tokenizer tokenizer = new Tokenizer(inputString);
        Expression expression = parseExpr(tokenizer);
        tokenizer.skipWhitespace();
        if (!tokenizer.isEnd()) {
            throw new IllegalArgumentException("Неожиданные символы в конце: "
                    + inputString.substring(tokenizer.position));
        }
        return expression;
    }

    /**
     * Рекурсивно разбирает выражение из токенов.
     *
     * @param tokenizer объект {@link Tokenizer}, предоставляющий доступ к символам
     * @return разобранное выражение
     * @throws IllegalArgumentException при синтаксической ошибке
     */
    private static Expression parseExpr(Tokenizer tokenizer) {
        tokenizer.skipWhitespace();
        if (tokenizer.peek() == '(') {
            tokenizer.consume(); // '('
            Expression left = parseExpr(tokenizer);
            tokenizer.skipWhitespace();
            char operator = tokenizer.consume();
            Expression right = parseExpr(tokenizer);
            tokenizer.skipWhitespace();
            if (tokenizer.peek() != ')') {
                throw new IllegalArgumentException("Ожидалась закрывающая скобка ) на позиции "
                        + tokenizer.position);
            }
            tokenizer.consume(); // ')'
            switch (operator) {
                case '+':
                    return new Add(left, right);
                case '-':
                    return new Sub(left, right);
                case '*':
                    return new Mul(left, right);
                case '/':
                    return new Div(left, right);
                default:
                    throw new IllegalArgumentException("Неизвестный оператор: " + operator);
            }
        } else {
            if (Character.isDigit(tokenizer.peek()) || tokenizer.peek() == '-') {
                int sign = 1;
                if (tokenizer.peek() == '-') {
                    sign = -1;
                    tokenizer.consume();
                }
                int value = 0;
                boolean found = false;
                while (!tokenizer.isEnd() && Character.isDigit(tokenizer.peek())) {
                    found = true;
                    value = value * 10 + (tokenizer.consume() - '0');
                }
                if (!found) {
                    throw new IllegalArgumentException("Неверное число на позиции "
                            + tokenizer.position);
                }
                return new Number(sign * value);
            } else if (Character.isLetter(tokenizer.peek())) {
                StringBuilder stringBuilder = new StringBuilder();
                while (!tokenizer.isEnd() && Character.isLetterOrDigit(tokenizer.peek())) {
                    stringBuilder.append(tokenizer.consume());
                }
                return new Variable(stringBuilder.toString());
            } else {
                throw new IllegalArgumentException(
                        "Неожиданный символ '" + tokenizer.peek() + "' на позиции "
                                + tokenizer.position
                );
            }
        }
    }

    /**
     * Разбирает строку с присваиваниями переменных.
     * Формат: {@code x=2; y=5; z=10}
     *
     * @param assignmentString строка с присваиваниями
     * @return отображение переменных и их значений
     * @throws IllegalArgumentException если формат некорректен
     */
    public static Map<String, Integer> parseAssignment(String assignmentString) {
        Map<String, Integer> map = new HashMap<>();
        if (assignmentString == null || assignmentString.trim().isEmpty()) {
            return map;
        }
        String[] parts = assignmentString.split(";");
        for (String part : parts) {
            String trimmedPart = part.trim();
            if (trimmedPart.isEmpty()) {
                continue;
            }
            String[] keyValue = trimmedPart.split("=");
            if (keyValue.length != 2) {
                throw new IllegalArgumentException("Некорректное присваивание: " + part);
            }
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            map.put(key, Integer.parseInt(value));
        }
        return map;
    }

    /**
     * Вспомогательный класс для последовательного чтения символов из строки.
     * Поддерживает пропуск пробелов, просмотр и извлечение символов.
     */
    private static class Tokenizer {
        /** Исходная строка. */
        private final String inputString;

        /** Текущая позиция чтения. */
        private int position;

        /**
         * Создаёт токенайзер для заданной строки.
         *
         * @param inputString исходная строка
         */
        Tokenizer(String inputString) {
            this.inputString = inputString;
            this.position = 0;
        }

        /**
         * Проверяет, достигнут ли конец строки.
         *
         * @return {@code true}, если больше нет символов
         */
        boolean isEnd() {
            skipWhitespace();
            return position >= inputString.length();
        }

        /** Пропускает пробельные символы. */
        void skipWhitespace() {
            while (position < inputString.length()
                    && Character.isWhitespace(inputString.charAt(position))) {
                position++;
            }
        }

        /**
         * Возвращает текущий символ без сдвига позиции.
         *
         * @return текущий символ или '\0', если достигнут конец
         */
        char peek() {
            skipWhitespace();
            if (position >= inputString.length()) {
                return '\0';
            }
            return inputString.charAt(position);
        }

        /**
         * Извлекает текущий символ и сдвигает позицию на один.
         *
         * @return извлечённый символ или '\0', если достигнут конец
         */
        char consume() {
            skipWhitespace();
            if (position >= inputString.length()) {
                return '\0';
            }
            return inputString.charAt(position++);
        }
    }
}