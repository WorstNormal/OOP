package ru.nsu.gaev;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {

    // Вспомогательный поддельный парсер для теста (если Parser.parseAssignment уже реализован — не нужен)
    // Например, строка "x=5,y=2" → Map.of("x", 5, "y", 2)
    static class Parser {
        public static Map<String, Integer> parseAssignment(String assignment) {
            Map<String, Integer> vars = new HashMap<>();
            String[] pairs = assignment.split(",");
            for (String pair : pairs) {
                String[] parts = pair.split("=");
                vars.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            }
            return vars;
        }
    }

    // --- Тестируем eval(String assignment) ---
    @Test
    void testEvalStringAssignment() {
        Expression expr = new Add(new Variable("x"), new Number(3)); // x + 3

        int result = expr.eval("x=7"); // должно вычислить 7 + 3 = 10
        assertEquals(10, result);
    }

    @Test
    void testEvalStringAssignmentMultipleVars() {
        Expression expr = new Mul(new Add(new Variable("x"), new Variable("y")), new Number(2)); // (x + y) * 2

        int result = expr.eval("x=3;y=5"); // (3 + 5) * 2 = 16
        assertEquals(16, result);
    }

    @Test
    void testEvalStringAssignmentWithSpaces() {
        Expression expr = new Sub(new Variable("a"), new Number(4)); // a - 4

        int result = expr.eval("a = 10 "); // 10 - 4 = 6
        assertEquals(6, result);
    }

    // --- Тестируем derivative() ---
    @Test
    void testDerivative() {
        Expression expr = new Add(new Variable("x"), new Number(5)); // x + 5
        Expression derivative = expr.derivative("x");

        // Производная (x + 5)' = 1 + 0 = 1
        assertEquals(new Number(1), derivative.simplify());
    }

    // --- Тестируем simplify() ---
    @Test
    void testSimplifyConstantExpression() {
        Expression expr = new Add(new Number(2), new Number(3)); // 2 + 3
        Expression simplified = expr.simplify();

        assertTrue(simplified instanceof Number);
        assertEquals(5, ((Number) simplified).getValue());
    }

    // --- Тестируем print() ---
    @Test
    void testPrint() {
        Expression expr = new Add(new Variable("x"), new Number(1)); // (x + 1)
        assertDoesNotThrow(expr::print);
    }

    // --- Тестируем toExpressionString() ---
    @Test
    void testToExpressionString() {
        Expression expr = new Div(new Add(new Variable("x"), new Number(2)), new Number(5)); // (x+2)/5
        String expected = "((x+2)/5)";
        assertEquals(expected, expr.toExpressionString());
    }
}
