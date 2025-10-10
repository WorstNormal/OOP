package ru.nsu.gaev;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс {@code Parser} выполняет синтаксический разбор арифметических выражений
 * и преобразует их в объекты типа {@link Expression}.
 * Также поддерживается разбор строк с присваиванием переменных.
 */
public class Parser {

    public static Expression parse(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Входная строка не может быть null");
        }
        Tokenizer t = new Tokenizer(s);
        Expression e = parseExpr(t);
        t.skipWhitespace();
        if (!t.isEnd()) {
            throw new IllegalArgumentException("Неожиданные символы в конце: " + s.substring(t.pos));
        }
        return e;
    }

    private static Expression parseExpr(Tokenizer t) {
        t.skipWhitespace();
        if (t.peek() == '(') {
            t.consume(); // '('
            Expression left = parseExpr(t);
            t.skipWhitespace();

            // Если после левого выражения сразу идёт закрывающая скобка, то это группировка: (expr)
            if (t.peek() == ')') {
                t.consume(); // ')'
                return left;
            }

            char op = t.consume();
            Expression right = parseExpr(t);
            t.skipWhitespace();
            if (t.peek() != ')') {
                throw new IllegalArgumentException("Ожидалась закрывающая скобка ) на позиции " + t.pos);
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
                throw new IllegalArgumentException("Неожиданный символ '" + t.peek() + "' на позиции " + t.pos);
            }
        }
    }

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
            try {
                int v = Integer.parseInt(val);
                map.put(key, v);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Значение переменной не является числом: '" + val + "'");
            }
        }
        return map;
    }

    private static class Tokenizer {
        private final String s;
        private int pos;

        Tokenizer(String s) {
            this.s = s;
            this.pos = 0;
        }

        boolean isEnd() {
            skipWhitespace();
            return pos >= s.length();
        }

        void skipWhitespace() {
            while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) {
                pos++;
            }
        }

        char peek() {
            skipWhitespace();
            if (pos >= s.length()) {
                return '\0';
            }
            return s.charAt(pos);
        }

        char consume() {
            skipWhitespace();
            if (pos >= s.length()) {
                return '\0';
            }
            return s.charAt(pos++);
        }
    }
}