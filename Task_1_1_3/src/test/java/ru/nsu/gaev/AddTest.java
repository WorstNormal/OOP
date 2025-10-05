package ru.nsu.gaev;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AddTest {

    // Проверка вычисления выражения
    @Test
    void testEval() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Expression expr = new Add(new Add(x, new ru.nsu.gaev.Number(2)), new Add(new ru.nsu.gaev.Number(3), y));

        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);
        vars.put("y", 10);

        int result = expr.eval(vars);
        assertEquals(20, result); // (5 + 2) + (3 + 10) = 20
    }

    // Проверка производной выражения
    @Test
    void testDerivative() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Expression expr = new Add(new Add(x, new ru.nsu.gaev.Number(2)), new Add(new ru.nsu.gaev.Number(3), y));

        // Производная от (x + 2 + 3 + y) по x
        Expression derivative = expr.derivative("x");

        // Производная от x = 1, производная от 2 и 3 = 0, производная от y = 0
        // Получим: 1 + 0 + 0 = 1
        Map<String, Integer> vars = new HashMap<>();
        int derivativeResult = derivative.eval(vars);
        assertEquals(1, derivativeResult);
    }

    // Проверка упрощения выражения
    @Test
    void testSimplify() {
        Expression expr = new Add(new ru.nsu.gaev.Number(2), new ru.nsu.gaev.Number(3));
        Expression simplifiedExpr = expr.simplify();

        // Ожидаем, что (2 + 3) будет упрощено до 5
        assertTrue(simplifiedExpr instanceof ru.nsu.gaev.Number);
        assertEquals(5, ((ru.nsu.gaev.Number) simplifiedExpr).getValue());
    }

    // Проверка строкового представления выражения
    @Test
    void testToExpressionString() {
        Expression x = new Variable("x");
        Expression expr = new Add(x, new ru.nsu.gaev.Number(2));

        String expressionString = expr.toExpressionString();
        assertEquals("(x+2)", expressionString);
    }

    // Проверка равенства выражений
    @Test
    void testEquals() {
        Expression x = new Variable("x");
        Expression expr1 = new Add(x, new ru.nsu.gaev.Number(2));
        Expression expr2 = new Add(x, new ru.nsu.gaev.Number(2));
        Expression expr3 = new Add(x, new ru.nsu.gaev.Number(3));

        assertEquals(expr1, expr2); // Они должны быть равны
        assertNotEquals(expr1, expr3); // Они не должны быть равны
    }

    // Проверка хеш-кодов
    @Test
    void testHashCode() {
        Expression x = new Variable("x");
        Expression expr1 = new Add(x, new ru.nsu.gaev.Number(2));
        Expression expr2 = new Add(x, new ru.nsu.gaev.Number(2));
        Expression expr3 = new Add(x, new ru.nsu.gaev.Number(3));

        assertEquals(expr1.hashCode(), expr2.hashCode()); // Хеш-коды должны быть одинаковыми
        assertNotEquals(expr1.hashCode(), expr3.hashCode()); // Хеш-коды должны быть разными
    }
}
