package ru.nsu.gaev;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс {@code Parser} выполняет синтаксический разбор арифметических выражений
 * и преобразует их в объекты типа {@link Expression}.
 * Также поддерживается разбор строк с присваиванием переменных.
 */
public class Parser {

    /**
     * Разбирает строковое представление выражения и возвращает объект {@link Expression}.
     *
     * @param s входная строка с выражением
     * @return разобранное выражение
     * @throws IllegalArgumentException при некорректном вводе
     */
    public static Expression parse(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Input string must not be null");
        }
        Tokenizer t = new Tokenizer(s);
        Expression e = parseExpr(t);
        t.skipWhitespace();
        if (t.hasMore()) {
            throw new IllegalArgumentException("Unexpected trailing symbols: "
                    + s.substring(t.pos));
        }
        return e;
    }

    private static Expression parseExpr(Tokenizer t) {
        t.skipWhitespace();
        if (t.peek() == '(') {
            t.consume(); // '('
            t.skipWhitespace();
            Expression left = parseExpr(t);
            if (t.peek() == ')') {
                t.consume(); // ')'
                return left;
            }
            t.skipWhitespace();
            if (t.peek() != ')') {
                throw new IllegalArgumentException("Missing closing ')' at position " + t.pos);
            }
            t.consume(); // ')'
            Expression right = parseExpr(t);
            char op = t.consume();
            return switch (op) {
                case '+' -> new Add(left, right);
                case '-' -> new Sub(left, right);
                case '*' -> new Mul(left, right);
                case '/' -> new Div(left, right);
                default -> throw new IllegalArgumentException("Unknown operator: " + op);
            };
        } else {
            if (Character.isDigit(t.peek()) || t.peek() == '-') {
                int sign = 1;
                if (t.peek() == '-') {
                    sign = -1;
                    t.consume();
                }
                int val = 0;
                boolean found = false;
                while (t.hasMore() && Character.isDigit(t.peek())) {
                    found = true;
                    val = val * 10 + (t.consume() - '0');
                }
                if (!found) {
                    throw new IllegalArgumentException("No digit found at position " + t.pos);
                }
                return new Number(sign * val);
            } else if (Character.isLetter(t.peek())) {
                StringBuilder sb = new StringBuilder();
                while (t.hasMore() && Character.isLetterOrDigit(t.peek())) {
                    sb.append(t.consume());
                }
                return new Variable(sb.toString());
            } else {
                throw new IllegalArgumentException("Unexpected symbol '" + t.peek() +
                        "' at position " + t.pos);
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
                throw new IllegalArgumentException("Invalid assignment: " + part);
            }
            String key = kv[0].trim();
            String val = kv[1].trim();
            try {
                int v = Integer.parseInt(val);
                map.put(key, v);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Value is not a number: '" + val + "'");
            }
        }
        return map;
    }

    private static class Tokenizer {
        private final String input;
        private int pos;

        Tokenizer(String input) {
            this.input = input;
            this.pos = 0;
        }

        boolean hasMore() {
            skipWhitespace();
            return pos < input.length();
        }

        void skipWhitespace() {
            while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
                pos++;
            }
        }

        char peek() {
            skipWhitespace();
            if (pos >= input.length()) {
                return '\0';
            }
            return input.charAt(pos);
        }

        char consume() {
            skipWhitespace();
            if (pos >= input.length()) {
                return '\0';
            }
            return input.charAt(pos++);
        }
    }
}