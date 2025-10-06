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
     * @param s строка с арифметическим выражением
     * @return объект {@link Expression}, соответствующий выражению
     * @throws IllegalArgumentException если входная строка некорректна
     */
    public static Expression parse(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Входная строка не может быть null");
        }
        Tokenizer t = new Tokenizer(s);
        Expression e = parseExpr(t);
        t.skipWhitespace();
        if (!t.isEnd()) {
            throw new IllegalArgumentException("Неожиданные символы в конце: " +
                    s.substring(t.pos));
        }
        return e;
    }

    /**
     * Рекурсивно разбирает выражение из токенов.
     *
     * @param t объект {@link Tokenizer}, предоставляющий последовательный доступ к символам
     * @return разобранное выражение
     * @throws IllegalArgumentException при синтаксической ошибке
     */
    private static Expression parseExpr(Tokenizer t) {
        t.skipWhitespace();
        if (t.peek() == '(') {
            t.consume(); // '('
            Expression left = parseExpr(t);
            t.skipWhitespace();
            char op = t.consume();
            Expression right = parseExpr(t);
            t.skipWhitespace();
            if (t.peek() != ')') {
                throw new IllegalArgumentException("Ожидалась закрывающая скобка ) на позиции "
                        + t.pos);
            }
            t.consume(); // ')'
            switch (op) {
                case '+':
                    return new Add(left, right);
                case '-':
                    return new Sub(left, right);
                case '*':
                    return new Mul(left, right);
                case '/':
                    return new Div(left, right);
                default:
                    throw new IllegalArgumentException("Неизвестный оператор: " + op);
            }
        } else {
            if (Character.isDigit(t.peek()) || t.peek() == '-') {
                int sign = 1;
                if (t.peek() == '-') {
                    sign = -1;
                    t.consume();
                }
                int val = 0;
                boolean found = false;
                while (!t.isEnd() && Character.isDigit(t.peek())) {
                    found = true;
                    val = val * 10 + (t.consume() - '0');
                }
                if (!found) {
                    throw new IllegalArgumentException("Неверное число на позиции " + t.pos);
                }
                return new Number(sign * val);
            } else if (Character.isLetter(t.peek())) {
                StringBuilder sb = new StringBuilder();
                while (!t.isEnd() && Character.isLetterOrDigit(t.peek())) {
                    sb.append(t.consume());
                }
                return new Variable(sb.toString());
            } else {
                throw new IllegalArgumentException(
                        "Неожиданный символ '" + t.peek() + "' на позиции " + t.pos
                );
            }
        }
    }

    /**
     * Разбирает строку с присваиваниями переменных.
     * Формат: {@code x=2; y=5; z=10}
     *
     * @param s строка с присваиваниями
     * @return отображение переменных и их значений
     * @throws IllegalArgumentException если формат некорректен
     */
    public static Map<String, Integer> parseAssignment(String s) {
        Map<String, Integer> map = new HashMap<>();
        if (s == null || s.trim().isEmpty()) {
            return map;
        }
        String[] parts = s.split(";");
        for (String part : parts) {
            String p = part.trim();
            if (p.isEmpty()) {
                continue;
            }
            String[] kv = p.split("=");
            if (kv.length != 2) {
                throw new IllegalArgumentException("Некорректное присваивание: " + part);
            }
            String key = kv[0].trim();
            String val = kv[1].trim();
            map.put(key, Integer.parseInt(val));
        }
        return map;
    }

    /**
     * Вспомогательный класс для последовательного чтения символов из строки.
     * Поддерживает пропуск пробелов, просмотр и извлечение символов.
     */
    private static class Tokenizer {
        /** Исходная строка. */
        private final String s;

        /** Текущая позиция чтения. */
        private int pos;

        /**
         * Создаёт токенайзер для заданной строки.
         *
         * @param s исходная строка
         */
        Tokenizer(String s) {
            this.s = s;
            this.pos = 0;
        }

        /**
         * Проверяет, достигнут ли конец строки.
         *
         * @return {@code true}, если больше нет символов
         */
        boolean isEnd() {
            skipWhitespace();
            return pos >= s.length();
        }

        /** Пропускает пробельные символы. */
        void skipWhitespace() {
            while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) {
                pos++;
            }
        }

        /**
         * Возвращает текущий символ без сдвига позиции.
         *
         * @return текущий символ или '\0', если достигнут конец
         */
        char peek() {
            skipWhitespace();
            if (pos >= s.length()) {
                return '\0';
            }
            return s.charAt(pos);
        }

        /**
         * Извлекает текущий символ и сдвигает позицию на один.
         *
         * @return извлечённый символ или '\0', если достигнут конец
         */
        char consume() {
            skipWhitespace();
            if (pos >= s.length()) {
                return '\0';
            }
            return s.charAt(pos++);
        }
    }
}
